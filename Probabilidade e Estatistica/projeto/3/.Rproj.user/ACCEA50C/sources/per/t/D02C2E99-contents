library(tidyverse)
library(ggplot2)


set.seed(722)
n=c(0100,0200,0300,0400,0500,0600,0700,0800,0900,1000,1100,1200,1300,
    1400,1500,1600,1700,1800,1900,2000,2100,2200,2300,2400,2500)
r = c()
lambda <- 4.35
for (i in 1:length(n)){
  
Samples <- replicate(1400, rexp(n[i], lambda))
lambdas_mle <- 1/colMeans(Samples)
lower_bounds <- lambdas_mle * (1 - ( qnorm(1-(0.09/2), mean=0, sd=1)) / sqrt(n))
upper_bounds <- lambdas_mle * (1 + ( qnorm(1-(0.09/2), mean=0, sd=1)) / sqrt(n))
amplitudes <- abs(upper_bounds - lower_bounds)
r[i] = mean(amplitudes)
}


r_c = r
for (i in 23:length(n)){
  
  Samples <- replicate(1400, rexp(n[i], 0.01))
  lambdas_mle <- 1/colMeans(Samples)
  lower_bounds <- lambdas_mle * (1 - ( qnorm(1-(0.09/2), mean=0, sd=1)) / sqrt(n))
  upper_bounds <- lambdas_mle * (1 + ( qnorm(1-(0.09/2), mean=0, sd=1)) / sqrt(n))
  amplitudes <- abs(upper_bounds - lower_bounds)
  r_c[i] = mean(amplitudes)
  print(i)
}
Ma = data.frame(n,r, r_c)
resPlot = pivot_longer(Ma, !n, 
                       names_to = c("Sem contaminacao", "Com_contaminacao"),
                       names_sep = "_",
                       values_to = "count")
ggplot(Ma, aes(x = n, y = r))+
  geom_point()