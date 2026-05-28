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
 	- **Componentes** — [Ver SVG](https://www.plantuml.com/plantuml/svg/hLZHSjks4dtdLs3qSfERQq_kQyRrH1QfXAaigvBirmvChcNK9C20e5sbpKm_eb_OBoaehJ5OWOARJFL4xZdONHpkBYY-RmtNfgkhn0XJ0RiHri1AO2cqN6jLLOhzzSUVRAgLD21BzePTyEAXltfLgbf8eTh_iSA2FHUqlMb1Fue2x9M6HhN2A6r9hvEaVH2ouPhNh18IpBO1fhIvLnvGLBnjnyPeSTiPoAtVbVW2xAr7QUzvgPw4tB0xNhNW8INSyQuoKoNDdDV0HYdymcywjkAo7IN9VzWFWpysNLxDrzayFO8dZTqmtrX_JeHNOxdfAgvVZzXlISBsQRDnqrogigl0jqnKtIW9qZZZHrL3Q9rmmokr2O65jZuBauBXeXfQfys0zHwA1zLvY8EcsRWpzwlzGs9dP_Yupiyz1YRp4acREEDAbw3Z5BkxEr714EbhalZwiPEJSvfy00UP1moQTf8W_GKlz174kKvvIYDQ97qEqRpeGpezFMVz_SIr6Nl7bjbgdT3mI4NJC5iRIjKcvFdP8d5d29YHfheOJtxy9ptbficgqNrJNoXbdjlAaKfLSo7HwfghAuNXjnLOCzlzJbYXWHienyOccClMS6aSrZNb8FRLTthTWdR-tZ1HMl9CoOspWCsaiiQLqS8pDtRnaz9bY6XLGTkRmPmjxVLv6BGtHuDANeFp7AuVbwM6jeswQ8q6C65wXJ3RqFf5D0DKrKcZjv54Lj0NnxTi1QtgIOBE7JbmZ1qOt-53Hnn7mfNG5beqHYWPWeqMHR_cGcnIA4JDgwFvt70jkFocj28na6zKyR0hKGCRq65i3EfEIHizljj7NdMnyi5vyTqLYATGT7tNWiu3gr4sSYX3u3etK4VTF-ur2IDyxgpPbdX4qFri-uxiMyDDrrhhVbhkxc9R6mxyR-xjssgORsjueNXvtVSySHHk7nsX0E5Gk1VDYu6CjhFUbjQygsz1kqeBW_NUOj5o-MXHfRUNwX7YOUCIxjvFtuN-rjO-CCB1T5ekuNC7RJoa1jugUNo7w2hOdtVUdVr-Sh8xgORiE9hsQ_26ZVu_OXOR_NTqCBeSovbySZ1hUhgR5u5J3DfdveDuF8NOszDJUzKtEFhLVd3N-JklsAKUxBedXD4p5kOntDLIWXg3x6g_cV4vBh9vciy_x7Eyca_pvMMMxkvM7_F50gydu_aacysUxwuk5xDidQN4D-cK5xrFn_cCEi2C8nxo-S_hvNY-ojUxspIRvJVPagP8VGvNEBhEvtlnhlVkbjagMzvaU3tBngkCkCBInUIGzjDrjbfZ1kF5OdbrawK8VSecY0I2kPS_zoTo-_pw9wG1VSSBs5NJqk4KtBrL7GMnOEEuQucZD6o52AuZYx6-Zn7sfHX7e-knt8xXM4qH0YsD29FCFyeY3xCqggyByhgThg_2DojgH9aFZPhIzeeG4qg0F99ApPl67Vl1Wx_SeKEAXmWMeWD86Fm_I_-w-7zYgHYX5HD281Hb2D03TF87D10e05nnX0XsUM1trU8W8eNtbIXKO-AmSEiKexi_WkbmaTE08bI644CbA1Y8CGZXjmqF2sgZwBGmMz8IdvwSvCJOgmJIY88ReUHKL0RqGV7iFr2qk3olcyhV1GQpwTo9JVXiUYjDfE7FN6TBkU4pnSjmB_1h0KsV6V7neLAtlCgUiOzSbeVojQhJ1SoLcQfEbexgSIvuYODb24sxfkf5WMTNZeGf7jHN_xc5DY2jCyS96e-M7DA2DYJLXnpIaRGAaK6xavIZ8mMz6bGcyeBMfLMAjA2FQRqYYqmb51Dl7KpcFPNDLvBGg6o-aWSqxRiA3bGYd5EIPaHbyvKaj0FLV2K9qUdcQ97JZyn4f4UFrOJH8wZ_-kOVYhXZ3_HElUlMHzpfch3X0O8qCd66vmYw8y9-JTw3BBkw-Xi0)
    
	![Component Diagram](https://www.plantuml.com/plantuml/svg/hLZHSjks4dtdLs3qSfERQq_kQyRrH1QfXAaigvBirmvChcNK9C20e5sbpKm_eb_OBoaehJ5OWOARJFL4xZdONHpkBYY-RmtNfgkhn0XJ0RiHri1AO2cqN6jLLOhzzSUVRAgLD21BzePTyEAXltfLgbf8eTh_iSA2FHUqlMb1Fue2x9M6HhN2A6r9hvEaVH2ouPhNh18IpBO1fhIvLnvGLBnjnyPeSTiPoAtVbVW2xAr7QUzvgPw4tB0xNhNW8INSyQuoKoNDdDV0HYdymcywjkAo7IN9VzWFWpysNLxDrzayFO8dZTqmtrX_JeHNOxdfAgvVZzXlISBsQRDnqrogigl0jqnKtIW9qZZZHrL3Q9rmmokr2O65jZuBauBXeXfQfys0zHwA1zLvY8EcsRWpzwlzGs9dP_Yupiyz1YRp4acREEDAbw3Z5BkxEr714EbhalZwiPEJSvfy00UP1moQTf8W_GKlz174kKvvIYDQ97qEqRpeGpezFMVz_SIr6Nl7bjbgdT3mI4NJC5iRIjKcvFdP8d5d29YHfheOJtxy9ptbficgqNrJNoXbdjlAaKfLSo7HwfghAuNXjnLOCzlzJbYXWHienyOccClMS6aSrZNb8FRLTthTWdR-tZ1HMl9CoOspWCsaiiQLqS8pDtRnaz9bY6XLGTkRmPmjxVLv6BGtHuDANeFp7AuVbwM6jeswQ8q6C65wXJ3RqFf5D0DKrKcZjv54Lj0NnxTi1QtgIOBE7JbmZ1qOt-53Hnn7mfNG5beqHYWPWeqMHR_cGcnIA4JDgwFvt70jkFocj28na6zKyR0hKGCRq65i3EfEIHizljj7NdMnyi5vyTqLYATGT7tNWiu3gr4sSYX3u3etK4VTF-ur2IDyxgpPbdX4qFri-uxiMyDDrrhhVbhkxc9R6mxyR-xjssgORsjueNXvtVSySHHk7nsX0E5Gk1VDYu6CjhFUbjQygsz1kqeBW_NUOj5o-MXHfRUNwX7YOUCIxjvFtuN-rjO-CCB1T5ekuNC7RJoa1jugUNo7w2hOdtVUdVr-Sh8xgORiE9hsQ_26ZVu_OXOR_NTqCBeSovbySZ1hUhgR5u5J3DfdveDuF8NOszDJUzKtEFhLVd3N-JklsAKUxBedXD4p5kOntDLIWXg3x6g_cV4vBh9vciy_x7Eyca_pvMMMxkvM7_F50gydu_aacysUxwuk5xDidQN4D-cK5xrFn_cCEi2C8nxo-S_hvNY-ojUxspIRvJVPagP8VGvNEBhEvtlnhlVkbjagMzvaU3tBngkCkCBInUIGzjDrjbfZ1kF5OdbrawK8VSecY0I2kPS_zoTo-_pw9wG1VSSBs5NJqk4KtBrL7GMnOEEuQucZD6o52AuZYx6-Zn7sfHX7e-knt8xXM4qH0YsD29FCFyeY3xCqggyByhgThg_2DojgH9aFZPhIzeeG4qg0F99ApPl67Vl1Wx_SeKEAXmWMeWD86Fm_I_-w-7zYgHYX5HD281Hb2D03TF87D10e05nnX0XsUM1trU8W8eNtbIXKO-AmSEiKexi_WkbmaTE08bI644CbA1Y8CGZXjmqF2sgZwBGmMz8IdvwSvCJOgmJIY88ReUHKL0RqGV7iFr2qk3olcyhV1GQpwTo9JVXiUYjDfE7FN6TBkU4pnSjmB_1h0KsV6V7neLAtlCgUiOzSbeVojQhJ1SoLcQfEbexgSIvuYODb24sxfkf5WMTNZeGf7jHN_xc5DY2jCyS96e-M7DA2DYJLXnpIaRGAaK6xavIZ8mMz6bGcyeBMfLMAjA2FQRqYYqmb51Dl7KpcFPNDLvBGg6o-aWSqxRiA3bGYd5EIPaHbyvKaj0FLV2K9qUdcQ97JZyn4f4UFrOJH8wZ_-kOVYhXZ3_HElUlMHzpfch3X0O8qCd66vmYw8y9-JTw3BBkw-Xi0)

	- **Funcional** — [Ver SVG](https://www.plantuml.com/plantuml/svg/RTFBQkim40RWlPvYo3wSsBbl3WcXbEv2IxCNqiGLo98PZKD9wRjN3exZ2AtCF_FfObdU1PR4RMs5nJC3Uo1JVJ5eGwZOU2U4LEm93jQWO-pJNjV6cS0ajQU-TaAdiOkY6ofTPH4cHqaAhrS9HolvxAcUm8y0Q0Cg6MB_7QlRD1J3FtXpHXb94334fISW0tmUYh5_-MwiZnoKP6bzvNjKZj492JJ6liNuX8QyRXNVwNnCNp5qRtcRaCWCQ3561y886jH6t_lBSVyegxI_UXhlNMWj3oRqQ3r67zuQPJgayU9jgnudiCdlk44w6zkRRTwWkvXXlMAMHuy5SQ15bWvdERWoxvBFKpmT-bvnAwsvHeU1fVQWyRRJWI_4hn3zbOFfz7zNItAPv7cI5qbU9dcLv7MIDqdU2l5qw-_RckNBHRvSvijp8VhVvo7JIaIxM6bh-mS0)
	    
		![Functional Diagram](https://www.plantuml.com/plantuml/svg/RTFBQkim40RWlPvYo3wSsBbl3WcXbEv2IxCNqiGLo98PZKD9wRjN3exZ2AtCF_FfObdU1PR4RMs5nJC3Uo1JVJ5eGwZOU2U4LEm93jQWO-pJNjV6cS0ajQU-TaAdiOkY6ofTPH4cHqaAhrS9HolvxAcUm8y0Q0Cg6MB_7QlRD1J3FtXpHXb94334fISW0tmUYh5_-MwiZnoKP6bzvNjKZj492JJ6liNuX8QyRXNVwNnCNp5qRtcRaCWCQ3561y886jH6t_lBSVyegxI_UXhlNMWj3oRqQ3r67zuQPJgayU9jgnudiCdlk44w6zkRRTwWkvXXlMAMHuy5SQ15bWvdERWoxvBFKpmT-bvnAwsvHeU1fVQWyRRJWI_4hn3zbOFfz7zNItAPv7cI5qbU9dcLv7MIDqdU2l5qw-_RckNBHRvSvijp8VhVvo7JIaIxM6bh-mS0)

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
