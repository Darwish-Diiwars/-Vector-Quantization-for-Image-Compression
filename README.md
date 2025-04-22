#🧠 Vector Quantization for Image Compression
# 🧠 Vector Quantization for Image Compression

Welcome to the official repository of my **DSAI 325 Assignment 4**: a full Java implementation of **Vector Quantization** for image compression using the **Linde–Buzo–Gray (LBG)** algorithm with codebook optimization.

> 💡 Compress smarter. Decode cleaner. Learn deeper.

---

## 🎯 Project Overview

This project demonstrates how vector quantization can be used to compress grayscale images with high efficiency. It features:

- 📸 Image preprocessing and grayscale conversion
- 🧊 Fixed-size block vectorization
- 🧠 Smart codebook generation using optimized clustering
- 🪄 Compression and reconstruction via LBG algorithm
- 📈 Automatic MSE & compression ratio calculation
- 📥 User-friendly input to define codebook size

---

## 🚀 How It Works

1. **Read an image** (`in.jpg`) and convert it to grayscale
2. **Split the image** into non-overlapping blocks (default: 4×4)
3. **Generate a codebook** of K representative vectors using:
   - Smart distance-maximized initialization (K-Means++ inspired)
   - K-Means refinement via LBG iteration
4. **Compress** by replacing blocks with the closest codebook vector
5. **Reconstruct the image** from the encoded vector indices
6. **Evaluate** performance:
   - 🧠 Mean Square Error (MSE)
   - 🧮 Compression Ratio

---

## 🧑‍💻 Getting Started

### ✅ Requirements

- Java JDK 8+
- VS Code or any Java-compatible IDE

### 📂 Project Structure

 ├── src/ │ └── VectorQuantization/ │ ├── Main.java │ ├── Compress.java │ ├── Decompress.java │ ├── ConstructVectors.java │ ├── Group.java │ ├── vector.java ├── in.jpg # Input image (grayscale or RGB) ├── codeBook.txt # Auto-generated compressed data ├── res.jpg # Output: decompressed image ├── README.md


### ▶️ Run the Program

From terminal:

```bash
javac -d . src/VectorQuantization/*.java
java VectorQuantization.Main

