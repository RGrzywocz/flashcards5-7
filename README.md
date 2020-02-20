# flashcards5-7
Flashcards consoleApp
Improve the application's interactivity. Ask the user for action and make it.

Support these actions:

add a card: add,
remove a card: remove,
load cards from file ("deserialization"): import,
save cards to file ("serialization"): export,
ask for a definition of some random cards: ask,
exit the program: exit.
You can use the following file format. The file consists of pairs of lines. The first line of each pair is a term, and the second line is a definition.

In this stage, if you try to add a card with an existing term or an existing definition, the application must just reject it by printing an error message (see example 1).

When you load cards from a file, you shouldn't erase the cards that aren't in the file. If the imported card already exists, it should update the old one (look at cards Japan and Moscow in the example 2). It is guaranteed, that there won't be any conflicts with definitions in the tests.
