This application takes three command-line arguments therefore we know what type of input to expect...

read command-line inputs → load an image → turn it into black and white → find white-pixel groups → save the results.

For args.length < 3 It checks if the user entered fewer than three arguments. If they did, the program prints a message showing how to run the application and stops running.