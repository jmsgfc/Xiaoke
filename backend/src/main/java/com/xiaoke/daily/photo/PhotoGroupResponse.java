package com.xiaoke.daily.photo;

import java.util.List;

public record PhotoGroupResponse(String month, List<Photo> photos) {
}

