# Create lib directory if it doesn't exist
New-Item -ItemType Directory -Force -Path "lib"

# Base URL for Maven Central
$baseUrl = "https://repo1.maven.org/maven2"

# Files to download
$files = @(
    "org/junit/jupiter/junit-jupiter-api/5.10.1/junit-jupiter-api-5.10.1.jar",
    "org/junit/jupiter/junit-jupiter-engine/5.10.1/junit-jupiter-engine-5.10.1.jar",
    "org/junit/platform/junit-platform-commons/1.10.1/junit-platform-commons-1.10.1.jar",
    "org/junit/platform/junit-platform-engine/1.10.1/junit-platform-engine-1.10.1.jar",
    "org/junit/platform/junit-platform-launcher/1.10.1/junit-platform-launcher-1.10.1.jar",
    "org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar"
)

# Download each file
foreach ($file in $files) {
    $url = "$baseUrl/$file"
    $fileName = $file.Split('/')[-1]
    $outputPath = "lib/$fileName"
    
    Write-Host "Downloading $fileName..."
    Invoke-WebRequest -Uri $url -OutFile $outputPath
}

Write-Host "All JUnit dependencies downloaded successfully!" 