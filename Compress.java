package VectorQuantization;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Compress {
    private ArrayList<vector> blocks = new ArrayList<>();
    private ArrayList<Group> averages = new ArrayList<>();
    private ArrayList<Integer> codes = new ArrayList<>();
    private int codeBookSize;
    private int h, w;
    private int height, width;

    Compress(String img_path, int k) {
        ConstructVectors c = new ConstructVectors(4, 4, img_path); // default block 4x4
        __init(c.blocks, k, 4, 4, c.hei, c.wi); // now passing custom k
    }
    

    void encode() throws IOException {
        System.out.println("Compressing...");
        split();
        getCodes();
        writeOnFile();
    }

    private void __init(ArrayList<vector> data, int k, int row, int col, int n, int m) {
        blocks.addAll(data);
        codeBookSize = k;
        h = row;
        w = col;
        height = n;
        width = m;

        vector initialVector = new vector(h, w);
        Group g = new Group(initialVector);
        g.setNeighbours(blocks);
        averages.add(g);
    }

    private void split() {
        while (averages.size() < codeBookSize) {
            ArrayList<Group> temp = new ArrayList<>();
            for (Group curr : averages) {
                vector curr_vector = curr.v;
                ArrayList<vector> neighbors = curr.neighbours;

                vector mean = new vector(curr_vector.height, curr_vector.width);
                for (int i = 0; i < mean.height; i++) {
                    for (int j = 0; j < mean.width; j++) {
                        double sum = 0.0;
                        for (vector v : neighbors) {
                            sum += v.data[i][j];
                        }
                        mean.data[i][j] = Math.ceil(sum / neighbors.size());
                    }
                }

                vector v1 = new vector(h, w);
                vector v2 = new vector(h, w);
                for (int i = 0; i < h; i++) {
                    for (int j = 0; j < w; j++) {
                        v1.data[i][j] = mean.data[i][j] - 1;
                        v2.data[i][j] = mean.data[i][j] + 1;
                    }
                }

                temp.add(new Group(v1));
                temp.add(new Group(v2));
            }

            assignVectorsToGroups(temp);
            averages = temp;
        }

        int stableCount = 0;
        while (true) {
            for (Group g : averages) {
                ArrayList<vector> nei = g.neighbours;
                vector avg = new vector(h, w);

                for (int i = 0; i < h; i++) {
                    for (int j = 0; j < w; j++) {
                        double sum = 0.0;
                        for (vector v : nei) {
                            sum += v.data[i][j];
                        }
                        if (nei.size() > 0) {
                            avg.data[i][j] = Math.ceil(sum / nei.size());
                        } else {
                            avg.data[i][j] = g.v.data[i][j];
                        }
                    }
                }

                if (equal(avg.data, g.v.data)) {
                    stableCount++;
                }

                g.v = avg;
                g.neighbours.clear();
            }

            assignVectorsToGroups(averages);

            if (stableCount == codeBookSize) break;
            stableCount = 0;
        }
    }

    private void assignVectorsToGroups(ArrayList<Group> groups) {
        for (vector curr : blocks) {
            int bestIndex = -1;
            double minDist = Double.MAX_VALUE;

            for (int i = 0; i < groups.size(); i++) {
                vector avg = groups.get(i).v;
                double dist = 0.0;

                for (int x = 0; x < h; x++) {
                    for (int y = 0; y < w; y++) {
                        dist += Math.abs(curr.data[x][y] - avg.data[x][y]);
                    }
                }

                if (dist < minDist) {
                    minDist = dist;
                    bestIndex = i;
                }
            }

            groups.get(bestIndex).add(curr);
        }
    }

    private void getCodes() {
        for (vector block : blocks) {
            boolean found = false;
            for (int i = 0; i < averages.size(); i++) {
                for (vector v : averages.get(i).neighbours) {
                    if (equal(block.data, v.data)) {
                        codes.add(i);
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
        }
    }

    private void writeOnFile() throws IOException {
        FileWriter f = new FileWriter("codeBook.txt");
        f.write(h + " " + w + " " + height + " " + width + "\n");

        for (Integer code : codes) f.write(code + " ");
        f.write("\n");

        for (Group g : averages) {
            vector v = g.v;
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    f.write(v.data[i][j] + " ");
                }
                f.write("\n");
            }
        }

        f.close();

        System.out.println("\n=== Compression Info ===");
        int originalSize = height * width * 8;
        int bitsPerCode = (int) Math.ceil(Math.log(averages.size()) / Math.log(2));
        int codesSize = codes.size() * bitsPerCode;
        int codebookSize = averages.size() * h * w * 8;
        int totalCompressed = codesSize + codebookSize;
        double ratio = (double) originalSize / totalCompressed;

        System.out.println("Original size: " + originalSize + " bits");
        System.out.println("Compressed codes size: " + codesSize + " bits");
        System.out.println("Codebook size: " + codebookSize + " bits");
        System.out.println("Total compressed size: " + totalCompressed + " bits");
        System.out.printf("Compression ratio: %.2f\n", ratio);
    }

    private boolean equal(double[][] a, double[][] b) {
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a[0].length; j++)
                if (a[i][j] != b[i][j])
                    return false;
        return true;
    }
}
