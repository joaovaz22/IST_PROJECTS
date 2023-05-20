library(readxl)
library(tidyverse)
data <- read_excel("C:/Users/joao_/OneDrive/Documentos/PE/projeto/4/Utentes.xlsx")

keeps <- c("Colesterol")
Colesterol = data[keeps]

keeps<- c("TAD")
TAD = data[keeps]
data <- data.frame(Colesterol, TAD)

ggplot(data, aes(y = TAD, x=Colesterol)) +
  geom_point()+
  labs(title = "Utentes",
       x = "Colesterol",
       y = "TAD",
  )
ggsave("Ex4.png")

#foto esta no 3