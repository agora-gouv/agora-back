package fr.gouv.agora.infrastructure.common

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class StrapiRichTextTest {
    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `returns html`() {
        // GIVEN
        @Language("JSON")
        val richTextFromStrapi = """
            [
              {
                "type": "heading",
                "level": 1,
                "children": [
                  {
                    "text": "titre 1",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "heading",
                "level": 2,
                "children": [
                  {
                    "text": "titre 2",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "heading",
                "level": 3,
                "children": [
                  {
                    "text": "titre 3",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "heading",
                "level": 4,
                "children": [
                  {
                    "text": "titre 4",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "heading",
                "level": 5,
                "children": [
                  {
                    "text": "titre 5",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "heading",
                "level": 6,
                "children": [
                  {
                    "text": "titre 6",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "paragraph",
                "children": [
                  {
                    "text": "Bonjour, je suis un paragraphe",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "paragraph",
                "children": [
                  {
                    "text": "Bonsoir, je suis un autre paragraphe, avec du ",
                    "type": "text"
                  },
                  {
                    "bold": true,
                    "text": "gras",
                    "type": "text"
                  },
                  {
                    "text": ", et du ",
                    "type": "text"
                  },
                  {
                    "bold": true,
                    "text": "gras à moi",
                    "type": "text"
                  },
                  {
                    "bold": true,
                    "text": "tié italique",
                    "type": "text",
                    "italic": true
                  },
                  {
                    "text": ", puis un peu de ",
                    "type": "text"
                  },
                  {
                    "bold": true,
                    "text": "tout.",
                    "type": "text",
                    "italic": true,
                    "underline": true,
                    "strikethrough": true
                  }
                ]
              },
              {
                "type": "paragraph",
                "children": [
                  {
                    "code": true,
                    "text": "Je suis du code",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "paragraph",
                "children": [
                  {
                    "text": "",
                    "type": "text"
                  },
                  {
                    "url": "http://localhost/",
                    "type": "link",
                    "children": [
                      {
                        "text": "Je suis un lien",
                        "type": "text"
                      }
                    ]
                  },
                  {
                    "text": "",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "list",
                "format": "unordered",
                "children": [
                  {
                    "type": "list-item",
                    "children": [
                      {
                        "text": "Liste non ordonnée 1",
                        "type": "text"
                      }
                    ]
                  },
                  {
                    "type": "list-item",
                    "children": [
                      {
                        "text": "Liste non ordonnée 2",
                        "type": "text"
                      }
                    ]
                  },
                  {
                    "type": "list",
                    "format": "unordered",
                    "children": [
                      {
                        "type": "list-item",
                        "children": [
                          {
                            "text": "sous liste 1",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "list",
                        "format": "unordered",
                        "children": [
                          {
                            "type": "list-item",
                            "children": [
                              {
                                "text": "sous sous liste 1-1",
                                "type": "text"
                              }
                            ]
                          }
                        ],
                        "indentLevel": 2
                      },
                      {
                        "type": "list-item",
                        "children": [
                          {
                            "text": "sous liste 2",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "list",
                        "format": "unordered",
                        "children": [
                          {
                            "type": "list-item",
                            "children": [
                              {
                                "text": "sous sous liste 2-1",
                                "type": "text"
                              }
                            ]
                          }
                        ],
                        "indentLevel": 2
                      }
                    ],
                    "indentLevel": 1
                  }
                ]
              },
              {
                "type": "paragraph",
                "children": [
                  {
                    "text": "",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "list",
                "format": "ordered",
                "children": [
                  {
                    "type": "list-item",
                    "children": [
                      {
                        "text": "liste ordonnée 1",
                        "type": "text"
                      }
                    ]
                  },
                  {
                    "type": "list",
                    "format": "ordered",
                    "children": [
                      {
                        "type": "list-item",
                        "children": [
                          {
                            "text": "liste ordonnée 1-a",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "type": "list",
                        "format": "ordered",
                        "children": [
                          {
                            "type": "list-item",
                            "children": [
                              {
                                "text": "liste ordonnée 1-a-I",
                                "type": "text"
                              }
                            ]
                          },
                          {
                            "type": "list-item",
                            "children": [
                              {
                                "text": "liste ordonnée 1-a-II",
                                "type": "text"
                              }
                            ]
                          }
                        ],
                        "indentLevel": 2
                      },
                      {
                        "type": "list-item",
                        "children": [
                          {
                            "text": "liste ordonnée 1-b",
                            "type": "text"
                          }
                        ]
                      }
                    ],
                    "indentLevel": 1
                  },
                  {
                    "type": "list-item",
                    "children": [
                      {
                        "text": "liste ordonnée 2",
                        "type": "text"
                      }
                    ]
                  },
                  {
                    "type": "list-item",
                    "children": [
                      {
                        "text": "liste ordonnée 3 avec du ",
                        "type": "text"
                      },
                      {
                        "bold": true,
                        "text": "gras ",
                        "type": "text"
                      },
                      {
                        "text": "et un",
                        "type": "text"
                      },
                      {
                        "bold": true,
                        "text": " ",
                        "type": "text"
                      },
                      {
                        "url": "http://localhost-gras/",
                        "type": "link",
                        "children": [
                          {
                            "bold": true,
                            "text": "lien gras",
                            "type": "text"
                          }
                        ]
                      },
                      {
                        "text": "",
                        "type": "text"
                      }
                    ]
                  }
                ]
              },
              {
                "type": "quote",
                "children": [
                  {
                    "text": "Je suis un citation avec du ",
                    "type": "text"
                  },
                  {
                    "bold": true,
                    "text": "gras",
                    "type": "text"
                  }
                ]
              },
              {
                "type": "code",
                "children": [
                  {
                    "text": "Je suis un block de code ",
                    "type": "text"
                  },
                  {
                    "bold": true,
                    "text": "gras",
                    "type": "text"
                  },
                  {
                    "text": ".",
                    "type": "text"
                  }
                ]
              }
            ]
        """.trimIndent()

        // WHEN
        val ref = object : TypeReference<List<StrapiRichText>>() {}
        val actual = objectMapper
            .readValue(richTextFromStrapi, ref)
            .toHtml()

        // THEN
        @Language("HTML")
        val expected = """
            <h1>titre 1</h1>
            <h2>titre 2</h2>
            <h3>titre 3</h3>
            <h4>titre 4</h4>
            <h5>titre 5</h5>
            <h6>titre 6</h6>
            Bonjour, je suis un paragraphe<br/>
            Bonsoir, je suis un autre paragraphe, avec du <b>gras</b>, et du <b>gras à moi</b><i><b>tié italique</b></i>, puis
                un peu de <del><u><i><b>tout.</b></i></u></del><br/>
            <code>Je suis du code</code><br/>
            <a href="http://localhost/">Je suis un lien</a><br/>
            <ul>
                <li>Liste non ordonnée 1</li>
                <li>Liste non ordonnée 2</li>
                <ul>
                    <li>sous liste 1</li>
                    <ul>
                        <li>sous sous liste 1-1</li>
                    </ul>
                    <li>sous liste 2</li>
                    <ul>
                        <li>sous sous liste 2-1</li>
                    </ul>
                </ul>
            </ul>
            <br/>
            <ol>
                <li>liste ordonnée 1</li>
                <ol>
                    <li>liste ordonnée 1-a</li>
                    <ol>
                        <li>liste ordonnée 1-a-I</li>
                        <li>liste ordonnée 1-a-II</li>
                    </ol>
                    <li>liste ordonnée 1-b</li>
                </ol>
                <li>liste ordonnée 2</li>
                <li>liste ordonnée 3 avec du <b>gras </b>et un<b> </b><a href="http://localhost-gras/"><b>lien gras</b></a></li>
            </ol>
            <blockquote>Je suis un citation avec du <b>gras</b></blockquote>Je suis un block de code <b>gras</b>.
        """.trimIndent()
        assertThat(actual).isEqualToIgnoringWhitespace(expected)
    }
}
