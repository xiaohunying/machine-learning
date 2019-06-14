~~~~
title: Dropout Regularization
authors:
- name: Liping Wu
  email: lipingwu@email.com
summary: Dropout is a regularization technique for reducing overfitting in neural networks.
publishDate: 01/16/2018
updateDate: 02/16/2018
tags: [regularization, overfitting]
~~~~

# Dropout Regularization

Dropout is a regularization technique for reducing **overfitting** in neural networks by preventing complex co-adaptations on training data. It is a very efficient way of performing model averaging with neural networks. The term **dropout** refers to dropping out units (both hidden and visible) in a neural network.

## Inverted Dropout

## Downside

The downside of dropout is that the cost function is not well defined. So we can not plot the graph of the number of iterations vs. results which can be used to check the performance of gradient descent.

Turn off dropout and plot the graph to make sure that the cost function is decreasing and then turn on dropout.




