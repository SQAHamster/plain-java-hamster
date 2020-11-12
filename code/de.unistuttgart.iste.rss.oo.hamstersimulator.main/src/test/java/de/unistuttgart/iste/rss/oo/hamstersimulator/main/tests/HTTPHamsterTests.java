package de.unistuttgart.iste.rss.oo.hamstersimulator.main.tests;

import com.google.gson.*;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Direction;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Location;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Mode;
import de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes.Size;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.Hamster;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.HamsterGame;
import de.unistuttgart.iste.rss.oo.hamstersimulator.external.model.TerritoryBuilder;
import de.unistuttgart.iste.rss.oo.hamstersimulator.server.http.client.HamsterClient;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

public class HTTPHamsterTests {

    private static int DELAY = 100;

    /**
     * waits for DELAY milliseconds
     */
    private void delay() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            fail("interrupted");
        }
    }

    /**
     * Creates a new HamsterGame, initializes an empty territory of size 5x5 with the hamster at 0,0 facing east
     * also starts the server and delays
     * @return the game
     */
    private HamsterGame createHamsterGame() {
        final HamsterGame game = new HamsterGame();
        final TerritoryBuilder builder = game.getNewTerritoryBuilder();
        builder.initializeTerritory(new Size(5, 5));
        builder.defaultHamsterAt(Location.from(0, 0), Direction.EAST, 0);
        game.initialize(builder);
        game.startGame();
        HamsterClient.startAndConnectToServer(game.getModelViewAdapter());
        delay();
        return game;
    }

    /**
     * Make a simple request to the HamsterHTTPServer
     * @param method the method to use, e.g. GET or POST
     * @param path the path to use
     * @return the response
     */
    private SimpleResponse request(final String method, final String path) {
        try {
            final URL url = new URL("http://localhost:8080" + path);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.connect();

            final BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder responseBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                responseBuilder.append(inputLine);
            }

            return new SimpleResponse(connection.getResponseCode(), responseBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e);
        }
        throw new IllegalStateException("not reachable");
    }

    /**
     * gets the latest game id known to the server
     * @return the latest / biggest gameId
     */
    private int getLatestGameId() {
        final SimpleResponse response = request("GET", "/gamesList");
        final JsonArray array = JsonParser.parseString(response.getResponse()).getAsJsonArray();
        return StreamSupport.stream(array.spliterator(), false)
                .mapToInt(JsonElement::getAsInt)
                .max().orElseThrow();
    }

    /**
     * requests the state of the specified game
     * @param gameId the id of the game
     * @return the response form the HamsterHTTPServer
     */
    private SimpleResponse requestState(final int gameId) {
        return request("GET", "/state?id=" + gameId + "&since=0");
    }

    /**
     * request all deltas of the specified game
     * @param gameId the id of the game
     * @return a list with all deltas returned by the HamsterHTTPServer
     */
    private JsonArray requestDeltas(final int gameId) {
        return JsonParser.parseString(requestState(gameId).getResponse())
                .getAsJsonObject().get("deltas").getAsJsonArray();
    }

    /**
     * asserts the mode of the provided gameId
     * throws an AssertionError if the mode is different
     * @param gameId the id of the game to check the mode
     * @param expected the expected mode
     */
    private void assertMode(final int gameId, final Mode expected) {
        final SimpleResponse response = requestState(gameId);
        final String mode = JsonParser.parseString(response.getResponse())
                .getAsJsonObject().get("mode").getAsString();
        assertEquals(expected, Mode.valueOf(mode));
    }

    /**
     * asserts the location of a specified tile content
     * throws an AssertionError if the found location is different
     * @param gameId the id of the game
     * @param contentId the id of the tile content to check, starts with 0, increases
     * @param expected the expected location
     */
    private void assertLocation(final int gameId, final int contentId, final Location expected) {
        final JsonArray deltas = requestDeltas(gameId);
        final JsonElement locationElement = StreamSupport.stream(deltas.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .filter(object -> object.get("type").getAsString().equals("ADD_TILE_CONTENT"))
                .filter(object -> object.get("tileContentId").getAsInt() == contentId)
                .map(object -> object.get("location"))
                .reduce((location1, location2) -> location2).orElseThrow();
        final Location location = new Gson().fromJson(locationElement, Location.class);
        assertEquals(expected, location);
    }

    /**
     * asserts the direction of a specified hamster
     * throws an AssertionError if the found direction is different
     * @param gameId the id of the game
     * @param hamsterId the id of the hamster
     * @param expected the expected direction
     */
    private void assertDirection(final int gameId, final int hamsterId, final Direction expected) {
        final JsonArray deltas = requestDeltas(gameId);
        final String direction = StreamSupport.stream(deltas.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .filter(object -> object.get("type").getAsString().equals("ROTATE_HAMSTER"))
                .filter(object -> object.get("tileContentId").getAsInt() == hamsterId)
                .map(object -> object.get("direction").getAsString())
                .reduce((location1, location2) -> location2).orElseThrow();
        assertEquals(expected, Direction.valueOf(direction));
    }

    /**
     * asserts the type of a specified tile content
     * throws an AssertionError if the found type is different
     * @param gameId the id of the game
     * @param contentId the id of the tile content
     * @param expected the expected type
     */
    private void assertTileContentType(final int gameId, int contentId, final String expected) {
        final JsonArray deltas = requestDeltas(gameId);
        assertTrue(StreamSupport.stream(deltas.spliterator(), false)
                .map(JsonElement::getAsJsonObject)
                .filter(object -> object.get("type").getAsString().equals("ADD_TILE_CONTENT"))
                .filter(object -> object.get("tileContentId").getAsInt() == contentId)
                .map(object -> object.get("contentType").getAsString())
                .allMatch(type -> type.equals(expected)));
    }


    @Test
    public void testRunningMode() {
        final HamsterGame game = createHamsterGame();
        final int gameId = getLatestGameId();
        assertMode(gameId, Mode.RUNNING);
        assertTileContentType(gameId, 0, "HAMSTER");
    }

    @Test
    public void testPause() {
        final HamsterGame game = createHamsterGame();
        final int gameId = getLatestGameId();
        final SimpleResponse response = request("POST", "/action?id=" + gameId + "&action=pause");
        assertEquals(200, response.getStatusCode());
        delay();
        assertMode(gameId, Mode.PAUSED);
        assertEquals(Mode.PAUSED, game.getCurrentGameMode());
    }

    @Test
    public void testAbort() {
        final HamsterGame game = createHamsterGame();
        final int gameId = getLatestGameId();
        final SimpleResponse response = request("POST", "/action?id=" + gameId + "&action=abort");
        assertEquals(200, response.getStatusCode());
        delay();
        assertMode(gameId, Mode.ABORTED);
        assertEquals(Mode.ABORTED, game.getCurrentGameMode());
    }

    @Test
    public void testResume() {
        final HamsterGame game = createHamsterGame();
        final int gameId = getLatestGameId();
        game.pauseGame();
        delay();
        final SimpleResponse response = request("POST", "/action?id=" + gameId + "&action=resume");
        assertEquals(200, response.getStatusCode());
        delay();
        assertMode(gameId, Mode.RUNNING);
        assertEquals(Mode.RUNNING, game.getCurrentGameMode());
    }

    @Test
    public void testMove() {
        final HamsterGame game = createHamsterGame();
        final int gameId = getLatestGameId();
        final Hamster hamster = game.getTerritory().getDefaultHamster();
        final Location location = hamster.getLocation();
        final Direction direction = hamster.getDirection();
        assertLocation(gameId, 0, location);
        hamster.move();
        delay();
        assertLocation(gameId, 0, location.translate(direction.getMovementVector()));
    }

    @Test
    public void testRotateHamster() {
        final HamsterGame game = createHamsterGame();
        final int gameId = getLatestGameId();
        final Hamster hamster = game.getTerritory().getDefaultHamster();
        final Direction direction = hamster.getDirection();
        assertDirection(gameId, 0, direction);
        hamster.turnLeft();
        delay();
        assertDirection(gameId, 0, direction.left());
    }

    @Test
    public void testSpawnHamster() {
        final HamsterGame game = createHamsterGame();
        final int gameId = getLatestGameId();
        final Hamster hamster = new Hamster(game.getTerritory(), Location.from(2,3), Direction.NORTH, 0);
        delay();
        assertTileContentType(gameId, 1, "HAMSTER");
        assertLocation(gameId, 1, hamster.getLocation());
        assertDirection(gameId, 1, hamster.getDirection());
    }

    @Test
    public void testUndoRedo() {
        final HamsterGame game = createHamsterGame();
        final int gameId = getLatestGameId();
        final Hamster hamster = game.getTerritory().getDefaultHamster();
        final Location location = hamster.getLocation();
        final Direction direction = hamster.getDirection();
        assertLocation(gameId, 0, location);
        hamster.move();
        game.stopGame();
        game.getModelViewAdapter().getGameController().undo();
        delay();
        assertLocation(gameId, 0, location);
        game.getModelViewAdapter().getGameController().redo();
        delay();
        assertLocation(gameId, 0, location.translate(direction.getMovementVector()));
    }


    private static class SimpleResponse {
        private final int statusCode;
        private final String response;

        public SimpleResponse(final int statusCode, final String response) {
            this.statusCode = statusCode;
            this.response = response;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getResponse() {
            return response;
        }
    }
}
