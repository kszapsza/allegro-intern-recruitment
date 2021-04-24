package com.kszapsza.allegrointernrecruitment.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class LinkHeaderParserTest {
    @Test
    public void shouldParseLinkHeaderWithNextAndLast() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/562236/repos?per_page=30&page=2>; rel=\"next\", " +
                "<https://api.github.com/user/562236/repos?per_page=30&page=3>; rel=\"last\"");

        // when
        Pagination pagination = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        Assertions.assertNull(pagination.getPrevPage());
        Assertions.assertEquals("?per_page=30&page=2", pagination.getNextPage());
        Assertions.assertEquals("?per_page=30&page=3", pagination.getLastPage());
        Assertions.assertNull(pagination.getFirstPage());
    }

    @Test
    public void shouldParseLinkHeaderWithAllLinks() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/6154722/repos?per_page=30&page=2>; rel=\"prev\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=4>; rel=\"next\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=132>; rel=\"last\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=1>; rel=\"first\"");

        // when
        Pagination pagination = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        Assertions.assertEquals("?per_page=30&page=2", pagination.getPrevPage());
        Assertions.assertEquals("?per_page=30&page=4", pagination.getNextPage());
        Assertions.assertEquals("?per_page=30&page=132", pagination.getLastPage());
        Assertions.assertEquals("?per_page=30&page=1", pagination.getFirstPage());
    }

    @Test
    public void shouldNotParseLinkHeaderWithInvalidRel() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/6154722/repos?per_page=30&page=30>; rel=\"foo\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=4>; rel=\"bar\", ");

        // when
        Pagination pagination = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        Assertions.assertNull(pagination.getPrevPage());
        Assertions.assertNull(pagination.getNextPage());
        Assertions.assertNull(pagination.getLastPage());
        Assertions.assertNull(pagination.getFirstPage());
    }

    @Test
    public void shouldParseLinkHeaderWithAdditionalQueryParams() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/6154722/repos?foo=abc&page=30&foo=1>; rel=\"prev\", " +
                "<https://api.github.com/user/6154722/repos?foo=abc&per_page=30&page=4>; rel=\"next\", ");

        // when
        Pagination pagination = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        Assertions.assertEquals("?foo=abc&page=30&foo=1", pagination.getPrevPage());
        Assertions.assertEquals("?foo=abc&per_page=30&page=4", pagination.getNextPage());
        Assertions.assertNull(pagination.getLastPage());
        Assertions.assertNull(pagination.getFirstPage());
    }
}