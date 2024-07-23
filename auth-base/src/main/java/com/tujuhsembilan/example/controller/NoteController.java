package com.tujuhsembilan.example.controller;

import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.example.controller.dto.NoteDto;
import com.tujuhsembilan.example.model.Note;
import com.tujuhsembilan.example.repository.NoteRepo;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

  private final NoteRepo repo;

  private final ModelMapper mdlMap;

  @GetMapping
  public ResponseEntity<?> getNotes(@AuthenticationPrincipal Jwt jwt) {
    String username = jwt.getClaimAsString("sub"); // Mengambil nama pengguna dari klaim JWT

    return ResponseEntity
        .ok(repo.findByOwner(username) // Mengambil catatan berdasarkan pemilik
            .stream()
            .map(o -> mdlMap.map(o, NoteDto.class))
            .collect(Collectors.toSet()));
  }

  @PostMapping
  public ResponseEntity<?> saveNote(@RequestBody NoteDto body, @AuthenticationPrincipal Jwt jwt) {
    String username = jwt.getClaimAsString("sub"); // Mengambil nama pengguna dari klaim JWT

    var newNote = mdlMap.map(body, Note.class);
    newNote.setOwner(username); // Set pemilik catatan ke pengguna yang terautentikasi
    newNote = repo.save(newNote);
    return ResponseEntity.status(HttpStatus.CREATED).body(newNote);
  }

}
