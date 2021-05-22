package dev.amatos.restcountries.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.amatos.restcountries.domain.ICountryRestSymbols;
import dev.amatos.restcountries.domain.ResponseEntity;
import dev.amatos.restcountries.service.v3.CountryService;
import dev.amatos.restcountries.v3.domain.Country;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Controller("v3/")
public class CountryControllerV3 {

  @Get(uri = "all", produces = MediaType.APPLICATION_JSON)
  public Object getAllCountries(@QueryValue("fields") Optional<String> fields) {
    List<Country> countries = CountryService.getInstance().getAll();
    return checkFieldsAndParseCountries(fields, countries);
  }

  private Object checkFieldsAndParseCountries(Optional<String> fields, List<Country> countries) {
    if (fields.isPresent()) {
      return parsedCountries(countries, fields.get());
    } else {
      return parsedCountries(countries, null);
    }
  }

  @Get("alpha/{alphacode}")
  public Object getByAlpha(@PathVariable("alphacode") String alpha,
      @QueryValue("fields") Optional<String> fields) {
    if (alpha.contains("codes")) {
      alpha = alpha.replace("codes=", "");
    }
    if (isEmpty(alpha) || alpha.length() < 2 || alpha.length() > 3) {
      return getResponse(Response.Status.BAD_REQUEST);
    }
    var country = CountryService.getInstance().getByAlpha(alpha);
    if (country != null) {
      return checkFieldsAndParseCountry(country, fields);
    }
    return getResponse(Response.Status.NOT_FOUND);
  }

  private Object checkFieldsAndParseCountry(Country country, Optional<String> fields) {
    if (fields.isPresent()) {
      return parsedCountry(country, fields.get());
    } else {
      return parsedCountry(country, null);
    }
  }

  @Get("alpha/")
  public Object getByAlphaList(@QueryParam("codes") String codes,
      @QueryParam("fields") Optional<String> fields) {
    if (isEmpty(codes) || codes.length() < 2 || (codes.length() > 3 && !codes.contains(","))) {
      return getResponse(Response.Status.BAD_REQUEST);
    }
    try {
      List<Country> countries = CountryService.getInstance().getByCodeList(codes);
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }

  }

  @Get("currency/{currency}")
  public Object getByCurrency(@PathVariable("currency") String currency,
      @QueryParam("fields") Optional<String> fields) {
    if (isEmpty(currency)) {
      return getResponse(Response.Status.BAD_REQUEST);
    }
    try {
      List<Country> countries = CountryService.getInstance().getByCurrency(currency);
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  @Get("name/{name}")
  public Object getByName(@PathVariable("name") String name,
      @QueryParam("fullText") Optional<Boolean> fullText,
      @QueryParam("fields") Optional<String> fields) {
    try {
      List<Country> countries = CountryService.getInstance()
          .getByName(name, fullText.orElse(false));
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  @Get("capital/{capital}")
  public Object getByCapital(@PathVariable("capital") String capital,
      @QueryParam("fields") Optional<String> fields) {
    try {
      List<Country> countries = CountryService.getInstance().getByCapital(capital);
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }


  @Get("region/{region}")
  public Object getByContinent(@PathVariable("region") String region,
      @QueryParam("fields") Optional<String> fields) {
    try {
      List<Country> countries = CountryService.getInstance().getByRegion(region);
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  @Get("subregion/{subregion}")
  public Object getBySubRegion(@PathVariable("subregion") String subregion,
      @QueryParam("fields") Optional<String> fields) {
    try {
      List<Country> countries = CountryService.getInstance().getBySubregion(subregion);
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  @Get("lang/{lang}")
  public Object getByLanguage(@PathVariable("lang") String language,
      @QueryParam("fields") Optional<String> fields) {
    try {
      List<Country> countries = CountryService.getInstance().getByLanguage(language);
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }

  }

  @Get("demonym/{demonym}")
  public Object getByDemonym(@PathVariable("demonym") String demonym,
      @QueryParam("fields") Optional<String> fields) {
    try {
      List<Country> countries = CountryService.getInstance().getByDemonym(demonym);
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  @Get("translation/{translation}")
  public Object getByTranslation(@PathVariable("translation") String translation,
      @QueryParam("fields") Optional<String> fields) {
    try {
      List<Country> countries = CountryService.getInstance().getByTranslation(translation);
      if (!countries.isEmpty()) {
        return checkFieldsAndParseCountries(fields, countries);
      }
      return getResponse(Response.Status.NOT_FOUND);
    } catch (Exception e) {
      return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  private Object parsedCountries(List<Country> countries, String excludedFields) {
    if (excludedFields == null || excludedFields.isEmpty()) {
      return countries;
    } else {
      return getCountriesJson(countries,
          Arrays.asList(excludedFields.split(ICountryRestSymbols.COLON)));
    }
  }

  private String getCountriesJson(List<Country> countries, List<String> fields) {
    var gson = new Gson();
    var parser = new JsonParser();
    var jsonArray = parser.parse(gson.toJson(countries)).getAsJsonArray();
    var resultArray = new JsonArray();
    for (var i = 0; i < jsonArray.size(); i++) {
      var jsonObject = (JsonObject) jsonArray.get(i);

      List<String> excludedFields = getExcludedFields(fields);
      for (String excludedField : excludedFields) {
        jsonObject.remove(excludedField);
      }
      resultArray.add(jsonObject);
    }
    return resultArray.toString();
  }

  private List<String> getExcludedFields(List<String> fields) {
    List<String> excludedFields = new ArrayList<>(Arrays.asList(COUNTRY_FIELDS));
    excludedFields.removeAll(fields);
    return excludedFields;
  }

  private Object getResponse(Response.Status status) {
    var gson = new Gson();
    return Response
        .status(status)
        .entity(gson.toJson(new ResponseEntity(status.getStatusCode(),
            status.getReasonPhrase()))).build().getEntity();
  }

  private Object parsedCountry(Country country, String fields) {
    if (fields == null || fields.isEmpty()) {
      return country;
    } else {
      return getCountryJson(country, Arrays.asList(fields.split(ICountryRestSymbols.COLON)));
    }
  }

  private String getCountryJson(Country country, List<String> fields) {
    var gson = new Gson();
    var parser = new JsonParser();
    var jsonObject = parser.parse(gson.toJson(country)).getAsJsonObject();

    List<String> excludedFields = getExcludedFields(fields);
    for (String field : excludedFields) {
      jsonObject.remove(field);
    }
    return jsonObject.toString();
  }

  private static final String[] COUNTRY_FIELDS = new String[]{
      "name",
      "tld",
      "cca2",
      "ccn3",
      "cca3",
      "cioc",
      "independent",
      "status",
      "unMember",
      "currencies",
      "idd",
      "capital",
      "altSpellings",
      "region",
      "subregion",
      "languages",
      "translations",
      "latlng",
      "landlocked",
      "borders",
      "area",
      "flag",
      "demonyms"
  };

  private boolean isEmpty(String value) {
    return value == null || value.isEmpty();
  }
}
