~~~~
title: Exponentially Weighted Averages
authors:
- name: Liping Wu
  email: lipingwu@email.com
summary: Exponentially Weighted Averages
publishDate: 01/16/2018
updateDate: 02/16/2018
tags: []
~~~~

# Exponentially Weighted Averages


For example, assume we have
$$\begin{matrix} \theta _{1}=40\\ \theta _{2}=49\\ \theta _{3}=45\\ \vdots \end{matrix}$$

we can define
$$\begin{matrix} V_{0}=0 \\ V_{1}=0.9V_{0}+0.1\theta _{1}\\ V_{2}=0.9V_{1}+0.1\theta _{2}\\ V_{3}=0.9V_{2}+0.1\theta _{3}\\ \vdots \end{matrix}$$

And we get
$${\color{Red} V_{t}=\beta V_{t-1}+(1-\beta ) \theta_{t}}$$


# Bias

There is bias in the initial phase


### Bias Correction

$$\frac{V_{t}}{1-\beta ^{t}}$$
