package de.containercloud.api.packets;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SuccessResponse {

    private final String response;
}