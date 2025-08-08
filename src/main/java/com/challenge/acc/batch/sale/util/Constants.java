package com.challenge.acc.batch.sale.util;

public class Constants {
  
  public static final String CSV_SALES_FILE_PARAM_NAME = "csvSalesFileName";
  
  public static final String[] CSV_SALE_ITEM_NAMES = {
    "pointOfSale",
    "amount",
    "quantity",
    "temperature",
    "customerId",
    "productId"
  };
  
  public static final String[] PROVINCE_NAMES = {
    "Buenos Aires",
    "Ciudad Autónoma de Buenos Aires",
    "Catamarca",
    "Chaco",
    "Chubut",
    "Córdoba",
    "Corrientes",
    "Entre Ríos",
    "Formosa",
    "Jujuy",
    "La Pampa",
    "La Rioja",
    "Mendoza",
    "Misiones",
    "Neuquén",
    "Río Negro",
    "Salta",
    "San Juan",
    "San Luis",
    "Santa Cruz",
    "Santa Fe",
    "Santiago del Estero",
    "Tierra del Fuego, Antártida e Islas del Atlántico Sur",
    "Tucumán"
  };
  
  public static final String SQL_SALE_INSERT_QUERY_UNNEST =
    "INSERT INTO sale ( point_of_sale, amount, quantity, taxes, customer_id, product_id, " +
      "creation_timestamp) SELECT * FROM UNNEST ( ?, ?, ?, ?, ?, ?, ?)";
  
  public static final Integer CHUNK_SIZE = 10000;
  
  public static final Integer MIN_TEMPERATURE = -50;
  public static final Integer MAX_TEMPERATURE = 50;
  
  public static final String POSTGRES_INTEGER = "integer";
  public static final String POSTGRES_NUMERIC = "numeric";
  public static final String POSTGRES_UUID = "uuid";
  public static final String POSTGRES_TIMESTAMP = "time with time zone";
  
  private Constants() {}
  
}
