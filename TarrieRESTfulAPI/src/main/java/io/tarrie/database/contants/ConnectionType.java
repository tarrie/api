package io.tarrie.database.contants;


public enum ConnectionType {
    Contact ("CONTACT"),
    Following ("FOLLOW"),
    Member ("MEMBER");

    private final String name;

    private ConnectionType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
