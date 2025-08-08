package com.challenge.acc.batch.sale.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "sales_master")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SaleMaster {
  
  @Id
  @Column(name = "master_id")
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,generator = "sales_master_id_seq")
  @SequenceGenerator(
    name = "sales_master_id_seq",
    sequenceName = "sales_master_id_seq",
    allocationSize = 1)
  Integer masterId;
  @Column(name = "file_name")
  String fileName;
  @Column(name = "creation_timestamp")
  OffsetDateTime creationTimestamp;
  @Column(name = "status")
  String status;
  @Column(name = "update_timestamp")
  OffsetDateTime updateTimestamp;
  
}
