library(readxl)
library(tidyverse)
ResiduosPerCapita <- read_excel("C:/Users/joao_/OneDrive/Documentos/PE/projeto/1/ResiduosPerCapita.xlsx", 
                                range = "A13:C43", col_names = c("paises", "2004", "2018"))
ResiduosPerCapita %>%
  pivot_longer(-paises, names_to = "year", values_to = "residuoPerCapita") %>%
  filter(paises == "GR - Grécia" |
           paises == "LU - Luxemburgo" |
           paises == "PL - Polónia") %>%
  ggplot(aes(y = residuoPerCapita, x = paises, fill = year)) +
  geom_bar(stat = "identity", position = "dodge") +
  labs(title = "Producao de residuos per capita",
       x = "Paises",
       y = "Toneladas de residuos per capita",
       fill = "Ano")
ggsave("Ex1.png")



