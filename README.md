# Parcezza Commerce

Plataforma de ecommerce para restaurantes con backend robusto en Spring Boot y frontend moderno en Angular. Este repo incluye la API, la SPA y el despliegue con Docker Compose para levantar todo el stack en un solo servidor.

Este repositorio sirve como proyecto final para la materia "Programación Orientada a Objetos"; contiene ejemplos de diseño por capas, modelos de dominio, y patrones de servicio/Repositorio que son útiles como referencia académica.

## Stack

- Backend: Java 21, Spring Boot 4.0.5, Spring Security (JWT), Spring Data JPA, Flyway
- Frontend: Angular 21 (standalone), TypeScript 5.9, SCSS
- DB: PostgreSQL 15
- Infra: Docker, Docker Compose, Nginx

## Vistas y diagramas

- Documentación (archivos):
	- Vista de Contexto: [docs/CONTEXT_DIAGRAM.md](docs/CONTEXT_DIAGRAM.md)
	- Vista Funcional: [docs/FUNCTIONAL_DIAGRAM.md](docs/FUNCTIONAL_DIAGRAM.md)
	- Vista Lógica (clases backend): [docs/CLASS_DIAGRAM.md](docs/CLASS_DIAGRAM.md)
	- Vista de Desarrollo (componentes y carpetas): [docs/COMPONENT_DIAGRAM.md](docs/COMPONENT_DIAGRAM.md)
	- Vista de Despliegue: [docs/DEPLOYMENT_DIAGRAM.md](docs/DEPLOYMENT_DIAGRAM.md)

- PlantUML (previsualización SVG — haga clic en "Ver SVG" para abrir en PlantUML):
	- **Componentes** — [Ver SVG](https://www.plantuml.com/plantuml/svg/RTFBQkim40RWlPvYo3wSsBbl3WcXbEv2IxCNqiGLo98PZKD9wRjN3exZ2AtCF_FfObdU1PR4RMs5nJC3Uo1JVJ5eGwZOU2U4LEm93jQWO-pJNjV6cS0ajQU-TaAdiOkY6ofTPH4R4hqaAhrS9HolvxAcUm8y0Q0Cg6MB_7QlRD1J3FtXpHXb94334fISW0tmUYh5_-MwiZnoKP6bzvNjKZj492JJ6liNuX8QyRXNVwNnCNp5qRtcRaCWCQ3561y886jH6t_lBSVyegxI_UXhlNMWj3oRqQ3r67zuQPJgayU9jgnudiCdlk44w6zkRRTwWkvXXlMAMHuy5SQ15bWvdERWoxvBFKpmT-bvnAwsvHeU1fVQWyRRJWI_4hn3zbOFfz7zNItAPv7cI5qbU9dcLv7MIDqdU2l5qw-_RckNBHRvSvijp8VhVvo7JIaIxM6bh-mS0)
    
		![Component Diagram](https://www.plantuml.com/plantuml/svg/RTFBQkim40RWlPvYo3wSsBbl3WcXbEv2IxCNqiGLo98PZKD9wRjN3exZ2AtCF_FfObdU1PR4RMs5nJC3Uo1JVJ5eGwZOU2U4LEm93jQWO-pJNjV6cS0ajQU-TaAdiOkY6ofTPH4R4hqaAhrS9HolvxAcUm8y0Q0Cg6MB_7QlRD1J3FtXpHXb94334fISW0tmUYh5_-MwiZnoKP6bzvNjKZj492JJ6liNuX8QyRXNVwNnCNp5qRtcRaCWCQ3561y886jH6t_lBSVyegxI_UXhlNMWj3oRqQ3r67zuQPJgayU9jgnudiCdlk44w6zkRRTwWkvXXlMAMHuy5SQ15bWvdERWoxvBFKpmT-bvnAwsvHeU1fVQWyRRJWI_4hn3zbOFfz7zNItAPv7cI5qbU9dcLv7MIDqdU2l5qw-_RckNBHRvSvijp8VhVvo7JIaIxM6bh-mS0)

	- **Funcional** — [Ver SVG](https://www.plantuml.com/plantuml/svg/XLPDZzis4BtxLmYvD2bW-nyKSVQZJT2qnIxIIv43LvoL2LCaEnmvSOlyzzRIYDPufEpDVEznJBuPIUzj9eFKjNwL3ouaWwQjwjYc623G8vqyL0WrcT1uMAsIgG-cWUhLFSP046prUXUQpXjyywhwTrLLHM1I6f2g-ikajA7y-RBOK-idorpdAOkn8yXo1sCPy0WegJg6PzVCOaB-RJKz7mJ2KuekK2xdR8qBBEQL-iFhjGiMlaxlay-MvUcL81mTnj12eBoTB6QFzGo6EeJBcVQn1IvK6p8-DhnA66rNqze26UT7oHxgG-o8bn4je5GaZC_E0wzCH_kjZuqBKm2XSPa0HyosBgmHgCEG1PRtBlNtqXUf8qAvHDCPj7bxBhxfVnVt2B26H16PdI0JnrqDpFFgcgiDqXB7jYtHlKTBP3AdlbKBD9kvH8_sBF63fKiiHPEljxRHWi_R7xNVUMyGR76yFC9v2ssYqpgJ8VYio7m03nJ3kZPeDTsVWU6ZySwQlbKPwWuTdJO5umduDdb5l3FreNz3F2PqeQdUnKZVUqkmVfUITxKX5uE-vV1SbostXiolqTWogezyi9jUn-27PCfQroECtWEMchkEzZSCbxf_30_R4lwdGMU2fX_Xl53mpJ2HkjemJZDwrBL_HmkeImm3gq_2ewY8Xtu69ImzuWaK1ZqEc7H1Wj8QveGlZ4bJ69IEN2jHr6Cl6996C2XTa2XR8B7tuGY18fuOrhOWf9ZTMLAS-PG17meyLeyU95WCK31xfd3ffQ8Ptn6XU-e8b82zLVZEMeISP_H8i-1xWbOGnVrvLEfbQnJEtH64RCR7U0If42ryWByxo89Ld_slDIHoCPIctI56V82SOiXZzt_sySdukr7xYmdsyiIyCvR_g0YO-HJEtwY8xX-mrzm3vDXX3Rz5kezTKFnjDxpsO1GAWRo4ZUTi8iPkCcoojsBWv3ZD3jV2ICb1cap4rN1Cctyz2BBzLqrVQlLSWySRTjQHUVAW7He_KCMZ3r_e_Dw2GFnjkNT-I3ZCbkUyM7hRnwOiYPiE4GATUV5ca7__FZoZgRRLBIGVBuUmiJu0hlllQuRDgOH6s-isUS_hNADBbBVZvUzsjn_lDgtbY_vGtGyniLh_LDNUGQ2Atz9Lbqq3go4sLkl_-Uy4eP8eNnJAiFcolkHGgPHXI-kMSvU8iYzKBHbNodHQcyHVeLLXKyRYbxGYBWkjZdmgI4kzZf3dxj-8bkVp53nnsBe4choQFI6xecAEpD92h0E8QhtIgu2ZkYKsg32ZHa5icCqvoutcBJfTQP_5dldOf9efjgX0rNTipC8k460mllhY-apyUk-QVUNX2FxDIhd0teXm8vQiK1zWRO-qxL9D0b8bAcedMAmobKA4JbHLPWaFIqMvPitY9_yj1Dkr_Zy0)
    
		![Functional Diagram](https://www.plantuml.com/plantuml/svg/XLPDZzis4BtxLmYvD2bW-nyKSVQZJT2qnIxIIv43LvoL2LCaEnmvSOlyzzRIYDPufEpDVEznJBuPIUzj9eFKjNwL3ouaWwQjwjYc623G8vqyL0WrcT1uMAsIgG-cWUhLFSP046prUXUQpXjyywhwTrLLHM1I6f2g-ikajA7y-RBOK-idorpdAOkn8yXo1sCPy0WegJg6PzVCOaB-RJKz7mJ2KuekK2xdR8qBBEQL-iFhjGiMlaxlay-MvUcL81mTnj12eBoTB6QFzGo6EeJBcVQn1IvK6p8-DhnA66rNqze26UT7oHxgG-o8bn4je5GaZC_E0wzCH_kjZuqBKm2XSPa0HyosBgmHgCEG1PRtBlNtqXUf8qAvHDCPj7bxBhxfVnVt2B26H16PdI0JnrqDpFFgcgiDqXB7jYtHlKTBP3AdlbKBD9kvH8_sBF63fKiiHPEljxRHWi_R7xNVUMyGR76yFC9v2ssYqpgJ8VYio7m03nJ3kZPeDTsVWU6ZySwQlbKPwWuTdJO5umduDdb5l3FreNz3F2PqeQdUnKZVUqkmVfUITxKX5uE-vV1SbostXiolqTWogezyi9jUn-27PCfQroECtWEMchkEzZSCbxf_30_R4lwdGMU2fX_Xl53mpJ2HkjemJZDwrBL_HmkeImm3gq_2ewY8Xtu69ImzuWaK1ZqEc7H1Wj8QveGlZ4bJ69IEN2jHr6Cl6996C2XTa2XR8B7tuGY18fuOrhOWf9ZTMLAS-PG17meyLeyU95WCK31xfd3ffQ8Ptn6XU-e8b82zLVZEMeISP_H8i-1xWbOGnVrvLEfbQnJEtH64RCR7U0If42ryWByxo89Ld_slDIHoCPIctI56V82SOiXZzt_sySdukr7xYmdsyiIyCvR_g0YO-HJEtwY8xX-mrzm3vDXX3Rz5kezTKFnjDxpsO1GAWRo4ZUTi8iPkCcoojsBWv3ZD3jV2ICb1cap4rN1Cctyz2BBzLqrVQlLSWySRTjQHUVAW7He_KCMZ3r_e_Dw2GFnjkNT-I3ZCbkUyM7hRnwOiYPiE4GATUV5ca7__FZoZgRRLBIGVBuUmiJu0hlllQuRDgOH6s-isUS_hNADBbBVZvUzsjn_lDgtbY_vGtGyniLh_LDNUGQ2Atz9Lbqq3go4sLkl_-Uy4eP8eNnJAiFcolkHGgPHXI-kMSvU8iYzKBHbNodHQcyHVeLLXKyRYbxGYBWkjZdmgI4kzZf3dxj-8bkVp53nnsBe4choQFI6xecAEpD92h0E8QhtIgu2ZkYKsg32ZHa5icCqvoutcBJfTQP_5dldOf9efjgX0rNTipC8k460mllhY-apyUk-QVUNX2FxDIhd0teXm8vQiK1zWRO-qxL9D0b8bAcedMAmobKA4JbHLPWaFIqMvPitY9_yj1Dkr_Zy0)

	- **Contexto** — [Ver SVG](https://www.plantuml.com/plantuml/svg/ROy_J_Cm48Vt-nIdJds7wjqPKEkM2YfKWbp1W1YEv8YY-azqlY2rYEzEd3HCtUpdUMJhjqnAefrta8vDQ4d8Onbz6mC7jNfoZCAbKgWT0r2fKN2sTarlUOQKy7AUrAhoJMYI2bLHXc0WatxXKF5aB3l70l3t0SuAafBFPyB2aNv6yMDuIieUlm7nRIinA8SAN_a3vwjGTuxattlEjdSvC5GUiryL3pYthJIXHXEZZeqPcY8chONjy-C0DmP-0E0o1HUBs_ujlC4zVN6TXy0mufeODrmpqDypDXdV7mw5_R-pJtim8zwO7kysPWrB3bNdtIy0)
    
		![Context Diagram](https://www.plantuml.com/plantuml/svg/ROy_J_Cm48Vt-nIdJds7wjqPKEkM2YfKWbp1W1YEv8YY-azqlY2rYEzEd3HCtUpdUMJhjqnAefrta8vDQ4d8Onbz6mC7jNfoZCAbKgWT0r2fKN2sTarlUOQKy7AUrAhoJMYI2bLHXc0WatxXKF5aB3l70l3t0SuAafBFPyB2aNv6yMDuIieUlm7nRIinA8SAN_a3vwjGTuxattlEjdSvC5GUiryL3pYthJIXHXEZZeqPcY8chONjy-C0DmP-0E0o1HUBs_ujlC4zVN6TXy0mufeODrmpqDypDXdV7mw5_R-pJtim8zwO7kysPWrB3bNdtIy0)

	- **Despliegue** — [Ver SVG](https://www.plantuml.com/plantuml/svg/RP9FRniX4CNlV8hHdlf7TjHQlVXGPVsdQLgfjRghvj8BXl4QXOKL34sigjyz0_R6bfBBBZ2_UJoUvZgI39HwAuI8zyODCiWUbEy7xz1HGmUB452HT9t52oJkfVOFndMmapPULZJkPBBKuYFLrdIkPnrG_C4W11bYmVgkWHKErXzAiGvgRmWLfO12EAyHgepKIldag89_GW3iM9mBlpVBqm60iZvfg6wpjaC6POHnKm2fo0Ue5i4_H0obVbeNWVFBe6fci4Zg7kcFUzEG9AFW3hUHVRqjVStis7Aqjrv-v9iYDola1ffDUzGczKUMgwD5E8S9rShp1G7MhZCE2tayUGNTIYuudSqC8SUyy9wEwejw-NtzO_LAq-0ZTG7Z_CEdGcv-DktDhtLJoF-buJaVc4m-0pzw3b_RTWDNvTUyHFWjBnZMOhLJacUXctKBncbydEwfj_2UBKIaU0cEvfbcNotKcrjGqjeenAbIgD4uOzzMYwKGhC_dPsTpQ33yHMWsTPuOGWYcsnFuNJOin0HqINPIqeauFR2Xa5oS2u1tS4gthCVGIbCPocdSi_ceWXaeNZ4a4_bU4e9-7jfCVmdU4Sk0YR1Dnb96UPiOaXera2zZav7iKOXhNgRUFW40)
    
		![Deployment Diagram](https://www.plantuml.com/plantuml/svg/RP9FRniX4CNlV8hHdlf7TjHQlVXGPVsdQLgfjRghvj8BXl4QXOKL34sigjyz0_R6bfBBBZ2_UJoUvZgI39HwAuI8zyODCiWUbEy7xz1HGmUB452HT9t52oJkfVOFndMmapPULZJkPBBKuYFLrdIkPnrG_C4W11bYmVgkWHKErXzAiGvgRmWLfO12EAyHgepKIldag89_GW3iM9mBlpVBqm60iZvfg6wpjaC6POHnKm2fo0Ue5i4_H0obVbeNWVFBe6fci4Zg7kcFUzEG9AFW3hUHVRqjVStis7Aqjrv-v9iYDola1ffDUzGczKUMgwD5E8S9rShp1G7MhZCE2tayUGNTIYuudSqC8SUyy9wEwejw-NtzO_LAq-0ZTG7Z_CEdGcv-DktDhtLJoF-buJaVc4m-0pzw3b_RTWDNvTUyHFWjBnZMOhLJacUXctKBncbydEwfj_2UBKIaU0cEvfbcNotKcrjGqjeenAbIgD4uOzzMYwKGhC_dPsTpQ33yHMWsTPuOGWYcsnFuNJOin0HqINPIqeauFR2Xa5oS2u1tS4gthCVGIbCPocdSi_ceWXaeNZ4a4_bU4e9-7jfCVmdU4Sk0YR1Dnb96UPiOaXera2zZav7iKOXhNgRUFW40)

	- **Clases** — [Ver SVG](https://www.plantuml.com/plantuml/svg/pLTVRzms37_tfn3oCg10YcROqn0SUcqtx81kBNBDtXcBkGgH9PUYtDt-VFT9jkL6KkBUbfU-I3vI97y_KgHzBnq3iUzqu-wKwO6W4wq6vxRCf6uyuwwrPg_-HF6YQKuw92QTs7gf66uqYh-2J8XpiMTIvY1QGc2KhuwpU6SSW-4att8XzxrSvFzyyN_ja1RNRsrmh6GU23jGEXVTUgr_WmvdwIjhDO8HQ2BGmhmFKJvRah-0-vWqo9TNmMGZAAmk1LUEBxTI4Zgt4J0ze7iQlHXcdLw44BdaTZEETLEj35xKek-B15Z7e5zRYRaSPAUCSap0Qi0j8NnDN_jehJTCwGhSp7NeINL0nvyr779MUzJwiN9FDy5-DaXbZAu7SumvAXM6eUMVwll1yCSkkPrYxXdOE-76hKYPFTXhqa-hytkoqhUyNYztvtE1gJX9T2sfdfKrwNAhmnjiLGTQt833zwHQN30n7f02AzlU5RdnH6ZQiZBGnbfccH5kt1waypkG0lCOgyIvd_QAQqsWZ-3V3KYavCCaCXHp-6K-hUD8O8Tf_rgc1z2-QBhNmA3jeGwpxibfVxW_GsOycvGOLqOXNci17vRxxkBv7MEt4IgiXREaVDZX5AiDosh9LXEOwlF91vdYOrrLRnIdQrbVmTfW758VB4FPSuGEQK1v7GBfdESxaaWdPYtDJlTHzNr0Cl-UjT5JEGvAW1p1RRiut1xbL89RgS84tiRrswn3qVnmxD3mgMJI-PKH6sWEAXbKqYkyBH03oRVW-8SQCfnSb7se_d-YaGnMU311UnSUuekmUCyZUp9N-CcZEpdaRBKIa12SDRdCM2w2xyo0XYqTVxK3hd3-NxUkSFtXsAFWiAnVomN_BSRQO3HRCDrNxEJ4fVbElB4TA1DK6bXP4zlNDMDpdrsSYVDpSVRY-VDdPwc_PzMpIHMs-AtLZ2-r9DwCbkEv7-TFYvbjTc2oIQeYMfwU9lzPd2rVM4rweome5tyviR35KTs4fVIP9doJ7ZBeoLkj3DON5Vhf2fR9IU8ixZBK9ksZaJFjIkXvIcG-HjicDLuGbQgyhMhzbtydbt-Vfo-QGfg05EA7FSmeIt62MCghzYWFP0IQbsYax_I_)
    
		![Class Diagram](https://www.plantuml.com/plantuml/svg/pLTVRzms37_tfn3oCg10YcROqn0SUcqtx81kBNBDtXcBkGgH9PUYtDt-VFT9jkL6KkBUbfU-I3vI97y_KgHzBnq3iUzqu-wKwO6W4wq6vxRCf6uyuwwrPg_-HF6YQKuw92QTs7gf66uqYh-2J8XpiMTIvY1QGc2KhuwpU6SSW-4att8XzxrSvFzyyN_ja1RNRsrmh6GU23jGEXVTUgr_WmvdwIjhDO8HQ2BGmhmFKJvRah-0-vWqo9TNmMGZAAmk1LUEBxTI4Zgt4J0ze7iQlHXcdLw44BdaTZEETLEj35xKek-B15Z7e5zRYRaSPAUCSap0Qi0j8NnDN_jehJTCwGhSp7NeINL0nvyr779MUzJwiN9FDy5-DaXbZAu7SumvAXM6eUMVwll1yCSkkPrYxXdOE-76hKYPFTXhqa-hytkoqhUyNYztvtE1gJX9T2sfdfKrwNAhmnjiLGTQt833zwHQN30n7f02AzlU5RdnH6ZQiZBGnbfccH5kt1waypkG0lCOgyIvd_QAQqsWZ-3V3KYavCCaCXHp-6K-hUD8O8Tf_rgc1z2-QBhNmA3jeGwpxibfVxW_GsOycvGOLqOXNci17vRxxkBv7MEt4IgiXREaVDZX5AiDosh9LXEOwlF91vdYOrrLRnIdQrbVmTfW758VB4FPSuGEQK1v7GBfdESxaaWdPYtDJlTHzNr0Cl-UjT5JEGvAW1p1RRiut1xbL89RgS84tiRrswn3qVnmxD3mgMJI-PKH6sWEAXbKqYkyBH03oRVW-8SQCfnSb7se_d-YaGnMU311UnSUuekmUCyZUp9N-CcZEpdaRBKIa12SDRdCM2w2xyo0XYqTVxK3hd3-NxUkSFtXsAFWiAnVomN_BSRQO3HRCDrNxEJ4fVbElB4TA1DK6bXP4zlNDMDpdrsSYVDpSVRY-VDdPwc_PzMpIHMs-AtLZ2-r9DwCbkEv7-TFYvbjTc2oIQeYMfwU9lzPd2rVM4rweome5tyviR35KTs4fVIP9doJ7ZBeoLkj3DON5Vhf2fR9IU8ixZBK9ksZaJFjIkXvIcG-HjicDLuGbQgyhMhzbtydbt-Vfo-QGfg05EA7FSmeIt62MCghzYWFP0IQbsYax_I_)

## Requerimientos funcionales (MVP real)

### RF-01: Autenticacion y sesiones JWT
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-01 |
| Nombre | Sistema de autenticacion |
| Entradas | Email, password, rememberMe |
| Salidas | Token JWT |
| Proceso | Registro o login con credenciales, emision de token |
| Criterios | Password hasheada con BCrypt, expiracion configurable, roles basicos |

### RF-02: Gestion de perfil y direcciones
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-02 |
| Nombre | Perfil y direcciones |
| Entradas | Datos personales y direcciones |
| Salidas | Perfil actualizado, lista de direcciones |
| Proceso | CRUD de perfil y direcciones bajo /api/me |
| Criterios | Validacion de datos y persistencia en BD |

### RF-03: Catalogo y productos publicos
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-03 |
| Nombre | Catalogo y detalle de productos |
| Entradas | Query, collection, paginacion |
| Salidas | Lista paginada y detalle de producto |
| Proceso | Consultas a /api/products y /api/catalogs |
| Criterios | Paginacion por pagina y tamano, acceso publico GET |

### RF-04: Variantes y gestion de productos (seller)
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-04 |
| Nombre | Variantes y CRUD de productos |
| Entradas | Datos de producto y variantes |
| Salidas | Producto o variante creada/actualizada |
| Proceso | Endpoints protegidos para rol SELLER |
| Criterios | Control de acceso por rol |

### RF-05: Carrito y reservas de inventario
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-05 |
| Nombre | Carrito con reserva |
| Entradas | Producto o variante, cantidad |
| Salidas | Carrito actualizado |
| Proceso | Add/Update/Remove/Clear en /api/cart |
| Criterios | Reserva temporal y liberacion automatica de stock |

### RF-06: Checkout y ordenes
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-06 |
| Nombre | Checkout y ordenes |
| Entradas | Carrito y direccion de entrega |
| Salidas | Orden creada, listado y detalle |
| Proceso | /api/orders/checkout, /api/orders, /api/orders/{id} |
| Criterios | Cancelacion disponible via /api/orders/{id}/cancel |

### RF-07: Pagos simulados
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-07 |
| Nombre | Confirmacion de pago |
| Entradas | Datos de pago |
| Salidas | Estado del pago |
| Proceso | /api/payments/confirm |
| Criterios | Validacion Luhn y actualizacion de estado |

### RF-08: Envio y tracking basico
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-08 |
| Nombre | Gestion de envios |
| Entradas | Orden o cambio de estado |
| Salidas | Estado del envio |
| Proceso | /api/shipments/order/{orderId}, listado admin |
| Criterios | Actualizacion de estado por rol ADMIN |

### RF-09: Devoluciones
| Aspecto | Descripcion |
| --- | --- |
| Codigo | RF-09 |
| Nombre | Solicitud y gestion de devoluciones |
| Entradas | Orden y motivo |
| Salidas | Estado de devolucion |
| Proceso | /api/returns/orders/{orderId}, listado admin |
| Criterios | Actualizacion de estado por rol ADMIN |

## Requerimientos no funcionales

### RNF-01: Seguridad
- JWT con expiracion configurable y CORS controlado.
- Passwords con BCrypt.
- Endpoints sensibles protegidos por roles (ADMIN, SELLER).

### RNF-02: Rendimiento
- Catalogo paginado para evitar cargas pesadas.
- Operaciones criticas en transacciones con reserva de inventario.

### RNF-03: Disponibilidad y despliegue
- Docker Compose con reinicio automatico en servicios.
- Migraciones automatizadas con Flyway.

### RNF-04: Mantenibilidad
- Arquitectura por capas (controller, service, repository, domain).
- Documentacion tecnica en [docs/PROJECT_EXPLANATION.md](docs/PROJECT_EXPLANATION.md).

### RNF-05: Compatibilidad
- API REST JSON.
- SPA responsive (Angular + SCSS).

## Como correr el proyecto

### Opcion 1: Docker Compose (recomendado)
1) Copia variables de entorno:

```bash
cp .env.example .env
```

2) Levanta todo el stack:

```bash
docker-compose up -d --build
```

- Frontend: http://localhost
- Backend: http://localhost:8080
- pgAdmin (opcional): http://localhost:5050

### Opcion 2: Desarrollo local

Backend:

```bash
cd backend
./mvnw spring-boot:run
```

Frontend:

```bash
cd parcezza-frontend
npm install
npm run start
```

## Variables de entorno principales

- DB: POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD
- Backend: SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD

## Notas y limites actuales

- No hay notificaciones en tiempo real
- No hay modulo de reportes ni analitica.
- La gestion de roles es basica (USER/ADMIN/SELLER) y se controla via API.
