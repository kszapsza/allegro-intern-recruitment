package com.kszapsza.allegrointernrecruitment.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

class LinkHeaderParserTest {
    @Test
    public void shouldParseLinkHeaderWithNextAndLast() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/562236/repos?per_page=30&page=2>; rel=\"next\", " +
                "<https://api.github.com/user/562236/repos?per_page=30&page=3>; rel=\"last\"");

        // when
        Pagination pagination = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        assertThat(pagination.getTotalPages(), equalTo(3L));
        assertThat(pagination.getPrevPage(), nullValue());
        assertThat(pagination.getNextPage(), equalTo("?per_page=30&page=2"));
        assertThat(pagination.getLastPage(), equalTo("?per_page=30&page=3"));
        assertThat(pagination.getFirstPage(), nullValue());
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
        assertThat(pagination.getTotalPages(), equalTo(132L));
        assertThat(pagination.getPrevPage(), equalTo("?per_page=30&page=2"));
        assertThat(pagination.getNextPage(), equalTo("?per_page=30&page=4"));
        assertThat(pagination.getLastPage(), equalTo("?per_page=30&page=132"));
        assertThat(pagination.getFirstPage(), equalTo("?per_page=30&page=1"));
    }

    @Test
    public void shouldNotParseLinkHeaderWithInvalidRel() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/6154722/repos?per_page=30&page=30>; rel=\"foo\", " +
                "<https://api.github.com/user/6154722/repos?per_page=30&page=4>; rel=\"bar\", ");

        // when
        Pagination pagination = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        assertThat(pagination.getTotalPages(), equalTo(1L));
        assertThat(pagination.getPrevPage(), nullValue());
        assertThat(pagination.getNextPage(), nullValue());
        assertThat(pagination.getLastPage(), nullValue());
        assertThat(pagination.getFirstPage(), nullValue());
    }

    @Test
    public void shouldParseLinkHeaderWithAdditionalQueryParams() {
        // given
        List<String> linkHeader = List.of("<https://api.github.com/user/6154722/repos?foo=abc&page=30&foo=1>; rel=\"prev\", " +
                "<https://api.github.com/user/6154722/repos?foo=abc&per_page=30&page=4>; rel=\"next\", ");

        // when
        Pagination pagination = LinkHeaderParser.parseLinks(linkHeader, "");

        // then
        assertThat(pagination.getTotalPages(), equalTo(1L));
        assertThat(pagination.getPrevPage(), equalTo("?foo=abc&page=30&foo=1"));
        assertThat(pagination.getNextPage(), equalTo("?foo=abc&per_page=30&page=4"));
        assertThat(pagination.getLastPage(), nullValue());
        assertThat(pagination.getFirstPage(), nullValue());
    }
}