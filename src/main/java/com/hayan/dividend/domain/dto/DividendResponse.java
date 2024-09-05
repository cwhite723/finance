package com.hayan.dividend.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

public record DividendResponse(@JsonSerialize(using = LocalDateSerializer.class)
                               @JsonDeserialize(using = LocalDateDeserializer.class)
                               LocalDate date,
                               String dividend) {
}
