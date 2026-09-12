package com.playlist.adapter;

import com.playlist.core.Track;
import java.util.List;
import java.util.Optional;

public interface TrackCatalog {

  /**
   * Lista todas as faixas válidas do catálogo.
   *
   * @return as faixas já convertidas para o modelo interno.
   */
  List<Track> findAll();

  /**
   * Busca uma faixa pelo identificador.
   *
   * @param id identificador da faixa (ex.: {@code "VNL-0001"}).
   * @return a faixa encontrada ou {@link Optional#empty()}.
   */
  Optional<Track> findById(String id);
}
