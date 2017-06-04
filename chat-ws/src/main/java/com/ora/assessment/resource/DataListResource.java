package com.ora.assessment.resource;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class DataListResource extends BaseResource {

  private final Collection<Object> data;

  public DataListResource(@NonNull Collection<Object> data) {
    super();
    this.data = data;
  }

  public <T> DataListResource(@NonNull Collection<T> source,
      @NonNull Function<T, ?> resourceFunction) {
    super();
    this.data = source.stream().map(s -> resourceFunction.apply(s)).collect(Collectors.toList());
  }

  public <T> DataListResource(@NonNull Page<T> source, @NonNull Function<T, ?> resourceFunction) {
    super();
    this.data = source.getContent().stream().map(s -> resourceFunction.apply(s))
        .collect(Collectors.toList());
    super.addMeta("pagination", source);
  }

  /**
   * Jackson MinIn for {@code Page}s.
   */
  public static abstract class PageMixIn<T> implements Page<T> {

    @Override
    @JsonProperty("per_page")
    public abstract int getSize();

    @Override
    @JsonProperty("current_page")
    public abstract int getNumber();

    @Override
    @JsonProperty("page_count")
    public abstract int getTotalPages();

    @Override
    @JsonProperty("total_count")
    public abstract long getTotalElements();

    @Override
    @JsonIgnore
    public abstract Sort getSort();

    @Override
    @JsonIgnore
    public abstract List<T> getContent();

    @Override
    @JsonIgnore
    public abstract int getNumberOfElements();

    @Override
    @JsonIgnore
    public abstract boolean isFirst();

    @Override
    @JsonIgnore
    public abstract boolean isLast();

  }
}
