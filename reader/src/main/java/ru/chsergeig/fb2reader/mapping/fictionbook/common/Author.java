package ru.chsergeig.fb2reader.mapping.fictionbook.common;

import jodd.jerry.Jerry;

import static ru.chsergeig.fb2reader.util.Utils.safeExtractValue;

public class Author {
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;

    private String toString = null;

    public Author(Jerry element) {
        firstName = safeExtractValue(() -> element.find("first-name").text(), "");
        lastName = safeExtractValue(() -> element.find("last-name").text(), "");
        nickName = safeExtractValue(() -> element.find("nickname").text(), "");
        email = safeExtractValue(() -> element.find("email").text(), "");
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        if (null == toString) {
            StringBuilder sb = new StringBuilder();
            if (null != firstName) {
                sb.append(firstName);
            }
            if (null != lastName) {
                sb.append(" ");
                sb.append(lastName);
                sb.append(".");
            }
            if (null != email) {
                sb.append(" ");
                sb.append(email);
            }
            if (null != nickName) {
                sb.append(" // ");
                sb.append(nickName);
            }
            toString = sb.toString();
        }
        return toString;
    }
}
