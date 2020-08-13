# Naive Bayes Spam Classifier
This project features a custom implementation of Naive Bayes algorithm for the purpose of classifying emails as spam or ham (not spam). The algorithm is a supervised learning algorithm which draws upon a set of labelled spam and ham emails, calculates the probability of words being in a spam or ham email, and then, using Bayes rule, uses the probabilities of a word being in a spam email to calculate the probability that if said word is seen in an email, it is a spam or ham email. 
## Performance
The performance of my implementation is as follows for the test dataset
Correctly classified as Ham:
328
Correctly classified as Spam:
145
Incorrectly classified as Ham:
0
Incorrectly classified as Spam:
27
This gives my algorithm an accuracy of 94.6% and an F-1 score of 0.9148. These numbers are very good for Naive Bayes.
## Instructions to run
Simply run the SpamFilterMain.class file to run the program. It will print out its ham/spam predictions for all the emails in the test dataset.
