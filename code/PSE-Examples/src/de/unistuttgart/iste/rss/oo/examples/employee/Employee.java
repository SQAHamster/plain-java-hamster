package de.unistuttgart.iste.rss.oo.examples.employee;

class Employee {

    public void storeName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException
                ("Employee names have to be non-null and non-empty");
        }
        internalStore(name);
    }

    private void internalStore(final String name) {
        assert name != null && !name.isEmpty();
        db.storeEmployeeName(name);
    }
}
