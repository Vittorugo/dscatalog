package com.devsuperior.dscatalog.resources.exceptions;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageError {
    private Instant time;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
