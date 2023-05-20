library(tidyverse)
library(ggplot2)

set.seed(735)  
n = 3
x<-replicate(50, runif(n, 10, 14))
x1 <- colMeans(x)

n = 27
x<-replicate(50, runif(n,10,14))
x2 <- colMeans(x)

n = 64
x<-replicate(50, runif(n,10,14))
x3 <- colMeans(x)

v1 = data.frame(x1)
v2 = data.frame(x2)
v3 = data.frame(x3)

g1 = ggplot(v1, aes(x1))  + 
  geom_histogram(aes(y = ..density..),breaks = seq(10, 14, by = 0.15), fill = "lightblue", colour = "black") + 
  geom_function(fun = dnorm, args = list(mean = mean(v1$x1), sd = sd(v1$x1)), colour = "red")+
  scale_y_continuous(sec.axis = sec_axis(~./6.7, name = "Frequência Relativa"))  +
  labs(title = "Histograma de distribuição em 50 distribuições uniformes contínuas no intervalo [10,14]
       amostra de n = 3",
   x = "Distribuição da média",
  y = "Densidade")
ggsave("n3.png")

g2 = ggplot(v2, aes(x2))  + 
  geom_histogram(aes(y = ..density..),breaks = seq(10, 14, by = 0.15), fill = "lightblue", colour = "black") + 
  geom_function(fun = dnorm, args = list(mean = mean(v2$x2), sd = sd(v2$x2)), colour = "red")+
scale_y_continuous(sec.axis = sec_axis(~./6.7, name = "Frequência Relativa"))+
  labs(title = "Histograma de distribuição em 50 distribuições uniformes contínuas no intervalo [10,14]
       amostra de n = 27",
       x = "Distribuição da média",
       y = "Densidade")
ggsave("n27.png")

g3 = ggplot(v3, aes(x3))  + 
  geom_histogram(aes(y = ..density..),breaks = seq(10, 14, by = 0.15), fill = "lightblue", colour = "black") + 
  geom_function(fun = dnorm, args = list(mean = mean(v3$x3), sd = sd(v3$x3)), colour = "red")+
  scale_y_continuous(sec.axis = sec_axis(~./6.7, name = "Frequência Relativa"))+
  labs(title = "Histograma de distribuição em 50 distribuições uniformes contínuas no intervalo [10,14]
       amostra de n = 64",
       x = "Distribuição da média",
       y = "Densidade")
ggsave("n64.png")



