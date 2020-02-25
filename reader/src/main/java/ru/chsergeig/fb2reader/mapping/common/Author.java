package ru.chsergeig.fb2reader.mapping.common;

import org.jsoup.nodes.Element;

import static ru.chsergeig.fb2reader.util.TextUtils.safeExtractValue;

public class Author {
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;

    private String toString = null;

    public Author(Element element) {
        firstName = safeExtractValue(() -> element.getElementsByTag("first-name").first().text());
        lastName = safeExtractValue(() -> element.getElementsByTag("last-name").first().text());
        nickName = safeExtractValue(() -> element.getElementsByTag("nickname").first().text());
        email = safeExtractValue(() -> element.getElementsByTag("email").first().text());
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
