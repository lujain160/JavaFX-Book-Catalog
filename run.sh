#!/bin/bash
echo "Compiling..."
javac --module-path /home/lujain/Downloads/openjfx-21.0.12_linux-x64_bin-sdk/javafx-sdk-21.0.12/lib --add-modules javafx.controls,javafx.fxml BookCatalogApp.java

if [ $? -eq 0 ]; then
    echo "Running..."
    java --module-path /home/lujain/Downloads/openjfx-21.0.12_linux-x64_bin-sdk/javafx-sdk-21.0.12/lib --add-modules javafx.controls,javafx.fxml BookCatalogApp
else
    echo "Compilation failed!"
fi
