library(readxl)
library(tidyverse)
NiveisOzono <- read_excel("C:/Users/joao_/OneDrive/Documentos/PE/projeto/3/QualidadeARO3.xlsx")

keeps <- c("Ihavo")
NiveisOzonoIhavo = NiveisOzono[keeps]

keeps <- c("Paio-Pires")
NiveisOzonoPaio = NiveisOzono[keeps]

Paio_Pires = as.numeric(unlist(NiveisOzonoPaio))
Ihavo = as.numeric(unlist(NiveisOzonoIhavo))

df = data.frame(Paio_Pires, Ihavo)
resPlot = pivot_longer(df, everything(), 
                       names_to = "Local",
                       values_to = "values")

ggplot(resPlot, aes(x=values, color=Local)) +
  geom_histogram(fill="white")+
labs(title = "Qualidade Ar - Ozono",
     x = "níveis de ozono, µg/m^3",
     y = "Frequência",
     )

ggsave("Ex3.png")