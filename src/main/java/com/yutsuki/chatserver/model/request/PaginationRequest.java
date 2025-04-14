package com.yutsuki.chatserver.model.request;

import com.yutsuki.chatserver.common.Pagination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PaginationRequest {

    @Schema(example = "1", defaultValue = "1", type = "number")
    private Integer page;

    @Schema(example = "10", defaultValue = "10", type = "number")
    private Integer size;

    public Pagination pagination() {
        return new Pagination(this.page, this.size, null);
    }
}
