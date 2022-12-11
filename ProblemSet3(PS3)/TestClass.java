public class TestClass{
    public static void main(String[] args) throws Exception{


        //empty file
        readFileAndCompress emptyFile = new readFileAndCompress();
        readFileAndCompress.loadFile("PS3/test1.txt");
        emptyFile.insertLetters();
        readFileAndCompress.allPriorityQueue();
        readFileAndCompress.bitRepresentation();
        readFileAndCompress.writeCompressed("PS3/test1.txt", "PS3/test1Compressed.txt");
        readFileAndCompress.decompressFile("PS3/test1Compressed.txt", "PS3/test1Decompressed.txt");


        //file with a single character.
        readFileAndCompress OnelinecompressFile = new readFileAndCompress();
        readFileAndCompress.loadFile("PS3/test2.txt");
        OnelinecompressFile.insertLetters();
        readFileAndCompress.allPriorityQueue();
        readFileAndCompress.bitRepresentation();
        readFileAndCompress.writeCompressed("PS3/test2.txt", "PS3/test2Compressed.txt");
        readFileAndCompress.decompressFile("PS3/test2Compressed.txt", "PS3/test2Decompressed.txt");

        //compressing and decompressing the file with one character only
        readFileAndCompress oneChar = new readFileAndCompress();
        readFileAndCompress.loadFile("PS3/test3.txt");
        oneChar.insertLetters();
        readFileAndCompress.allPriorityQueue();
        readFileAndCompress.bitRepresentation();
        readFileAndCompress.writeCompressed("PS3/test3.txt", "PS3/test3Compressed.txt");
        readFileAndCompress.decompressFile("PS3/test3Compressed.txt", "PS3/test3Decompressed.txt");


        //compressing and decompressing the USConstitution file
        readFileAndCompress USConstitution = new readFileAndCompress();
        readFileAndCompress.loadFile("PS3/USConstitution.txt");
        USConstitution.insertLetters();
        readFileAndCompress.allPriorityQueue();
        readFileAndCompress.bitRepresentation();
        readFileAndCompress.writeCompressed("PS3/USConstitution.txt", "PS3/USConstitutionCompressed.txt");
        readFileAndCompress.decompressFile("PS3/USConstitutionCompressed.txt", "PS3/USConstitutionDecompressed.txt");

        //compressing and decompressing the War and Peace txt file
        readFileAndCompress WarAndPeace = new readFileAndCompress();
        readFileAndCompress.loadFile("PS3/WarAndPeace.txt");
        WarAndPeace.insertLetters();
        readFileAndCompress.allPriorityQueue();
        readFileAndCompress.bitRepresentation();
        readFileAndCompress.writeCompressed("PS3/WarAndPeace.txt", "PS3/WarAndPeaceCompressed.txt");
        readFileAndCompress.decompressFile("PS3/WarAndPeaceCompressed.txt", "PS3/WarAndPeaceDecompressed.txt");

    }
}
