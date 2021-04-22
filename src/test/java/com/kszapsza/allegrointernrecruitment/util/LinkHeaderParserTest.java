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
        Links links = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        Assertions.assertNull(links.getPrevPage());
        Assertions.assertEquals("?per_page=30&page=2", links.getNextPage());
        Assertions.assertEquals("?per_page=30&page=3", links.getLastPage());
        Assertions.assertNull(links.getFirstPage());
    }

    @Test
    public void shouldParseLinkHeaderWithAllLinks() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/6154722/repos?per_page=30&page=2>; rel=\"prev\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=4>; rel=\"next\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=132>; rel=\"last\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=1>; rel=\"first\"");

        // when
        Links links = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        Assertions.assertEquals("?per_page=30&page=2", links.getPrevPage());
        Assertions.assertEquals("?per_page=30&page=4", links.getNextPage());
        Assertions.assertEquals("?per_page=30&page=132", links.getLastPage());
        Assertions.assertEquals("?per_page=30&page=1", links.getFirstPage());
    }

    @Test
    public void shouldNotParseLinkHeaderWithInvalidRel() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/6154722/repos?per_page=30&page=30>; rel=\"foo\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=4>; rel=\"bar\", ");

        // when
        Links links = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        Assertions.assertNull(links.getPrevPage());
        Assertions.assertNull(links.getNextPage());
        Assertions.assertNull(links.getLastPage());
        Assertions.assertNull(links.getFirstPage());
    }

    @Test
    public void shouldParseLinkHeaderWithAdditionalQueryParams() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/6154722/repos?foo=abc&page=30&foo=1>; rel=\"prev\", " +
                "<https://api.github.com/user/6154722/repos?foo=abc&per_page=30&page=4>; rel=\"next\", ");

        // when
        Links links = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        Assertions.assertEquals("?foo=abc&page=30&foo=1", links.getPrevPage());
        Assertions.assertEquals("?foo=abc&per_page=30&page=4", links.getNextPage());
        Assertions.assertNull(links.getLastPage());
        Assertions.assertNull(links.getFirstPage());
    }
}