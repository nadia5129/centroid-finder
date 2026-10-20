This application takes three command-line arguments therefore we know what type of input to expect...

read command-line inputs → load an image → turn it into black and white → find white-pixel groups → save the results.

The imports:
import java.awt.image.BufferedImage; -
 holds a picture in memory so the program can look at each pixels colors.

import java.io.File; -
points to a file on your computer like image.png

import java.io.PrintWriter; -
writes text into a file. Here it creates groups.csv

import java.util.List; -
A container that holds many items in order. Here it holds all the groups the program finds. 

import javax.imageio.ImageIO; -
opens an image file and saves an image file. It loads your input picture and saves binarized.png