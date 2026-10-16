package com.todolist.dtos;

import java.util.List;

public record PageResponseDTO<T>(
  List<T> content,
  int page,
  int size,
  int totalPages,
  long totalElements,
  boolean first,
  boolean last
) {}
