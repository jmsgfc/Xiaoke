package com.xiaoke.daily.common;

import java.util.List;

public record PageResponse<T>(List<T> records, int page, int pageSize, long total) {
}

