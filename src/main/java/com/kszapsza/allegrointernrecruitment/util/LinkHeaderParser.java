package com.kszapsza.allegrointernrecruitment.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkHeaderParser {
    private static final Pattern uriPattern = Pattern.compile("<.*(\\?.*&?page=\\d+.*)>");
    private static final Pattern rolePattern = Pattern.compile("rel=\"(.*)\"");

    private LinkHeaderParser() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static Links parseLinks(List<String> linkHeader, String baseUri) {
        String[] linkGroups = linkHeader.get(0).split(", ");
        Links links = new Links();

        for (String linkGroup : linkGroups) {
            Matcher uriMatcher = uriPattern.matcher(linkGroup);
            Matcher roleMatcher = rolePattern.matcher(linkGroup);

            if (uriMatcher.find() && roleMatcher.find()) {
                switch (roleMatcher.group(1)) {
                    case "prev":
                        links.setPrevPage(baseUri + uriMatcher.group(1));
                        break;
                    case "next":
                        links.setNextPage(baseUri + uriMatcher.group(1));
                        break;
                    case "first":
                        links.setFirstPage(baseUri + uriMatcher.group(1));
                        break;
                    case "last":
                        links.setLastPage(baseUri + uriMatcher.group(1));
                        break;
                }
            }
        }

        return links;
    }
}
