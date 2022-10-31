package com.alexander.rickmorty

import com.alexander.rickmorty.model.dto.*
import com.alexander.rickmorty.model.db.Character
import org.junit.Test
import kotlin.test.assertEquals

class CharacterMapperTest {

  @Test
  fun modelToDbCharacter() {
    val characterDto = CharacterDto(
      id = ID,
      name = NAME,
      status = STATUS,
      species = SPECIES,
      type = TYPE,
      gender = GENDER,
      image = IMAGE,
      episode = EPISODES,
      url = URL,
      created = CREATED
    )

    val character = Character(
      id = ID,
      name = NAME,
      status = STATUS,
      species = SPECIES,
      type = TYPE,
      gender = GENDER,
      origin = "",
      location = "",
      image = IMAGE,
      url = URL,
      created = CREATED
    )

    val characterConverted: Character = characterDto.toCharacter()
    assertEquals(character, characterConverted)
  }

  companion object {
    const val ID = 1
    const val NAME = "name"
    const val STATUS = "alive"
    const val SPECIES = "species"
    const val TYPE = "type"
    const val GENDER = "gender"
    const val IMAGE = "image"
    val EPISODES = listOf(
      "url_1",
      "url_2",
      "url_3",
    )
    const val URL = "url"
    const val CREATED = "created"
  }
}