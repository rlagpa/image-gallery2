package com.example.imagesgallery.libraries;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Created by hyemi
 *
 * kind of learning test to check that lombok works fine.
 * <p>
 *     1.install lombok plugin
 *     2.turn on annotation processing : other setting -> default setting -> build -> compile -> annotation processing
 *     3.add dependency
 *          provided "org.projectlombok:lombok:1.16.8"
 *          provided "org.glassfish:javax.annotation:10.0-b28"
 *     4.add lombok config file "lombok.config" on project root
 *          lombok.anyConstructor.suppressConstructorProperties=true
 * </p>
 */
public class LombokTest {

    public static final String DATA = "my-test-data";
    public static final int INDEX = 79;

    @Test
    public void testGetter() { //also covers builder test
        //when
        LombokTestDto dto = LombokTestDto.builder()
                .data(DATA)
                .index(INDEX)
                .build();

        //then
        assertThat(dto.getData(), is(DATA));
        assertThat(dto.getData(), is(not(DATA+"mutated")));
        assertThat(dto.getIndex(),is(INDEX));
        assertThat(dto.getIndex(),is(not(INDEX+1)));
    }

    @Test
    public void testToString() { //also covers setter test
        //when
        LombokTestDto dto = new LombokTestDto();
        dto.setData(DATA);
        dto.setIndex(INDEX);

        //then
        assertThat(dto.toString(),is(containsString(DATA)));
        assertThat(dto.toString(),is(containsString(String.valueOf(INDEX))));
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static public class LombokTestDto {
        private String data;
        private int index;
    }
}
