package com.kszapsza.allegrointernrecruitment.util;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkHeaderParser {
    private static final Pattern uriPattern = Pattern.compile("<.*(\\?.*&?page=\\d+.*)>");
    private static final Pattern rolePattern = Pattern.compile("rel=\"(.*)\"");

    private LinkHeaderParser() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static Pagination parseLinks(List<String> linkHeader, String baseUri) {
        String[] linkGroups = linkHeader.get(0).split(", ");
        Pagination pagination = new Pagination();

        for (String linkGroup : linkGroups) {
            Matcher uriMatcher = uriPattern.matcher(linkGroup);
            Matcher roleMatcher = rolePattern.matcher(linkGroup);

            if (uriMatcher.find() && roleMatcher.find()) {
                switch (roleMatcher.group(1)) {
                    case "prev":
                        pagination.setPrevPage(baseUri + uriMatcher.group(1));
                        break;
                    case "next":
                        pagination.setNextPage(baseUri + uriMatcher.group(1));
                        break;
                    case "first":
                        pagination.setFirstPage(baseUri + uriMatcher.group(1));
                        break;
                    case "last":
                        pagination.setLastPage(baseUri + uriMatcher.group(1));
                        break;
                }
            }
        }

        long totalPages = evaluateTotalPages(pagination).orElse(1L);
        pagination.setTotalPages(totalPages);

        return pagination;
    }

    private static Optional<Long> evaluateTotalPages(Pagination pagination) {
        if (pagination == null || pagination.getLastPage() == null) {
            return Optional.empty();
        }

        String lastPageUri = pagination.getLastPage();
        Pattern pagePattern = Pattern.compile("[&?]page=(\\d+).*");
        Matcher pageMatcher = pagePattern.matcher(lastPageUri);

        if (pageMatcher.find()) {
            return Optional.of(Long.parseLong(pageMatcher.group(1)));
        } else return Optional.empty();
    }
}
