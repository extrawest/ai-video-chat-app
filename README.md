# Video Insights Assistant

[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)]()
[![Maintainer](https://img.shields.io/static/v1?label=Yevhen%20Ruban&message=Maintainer&color=red)](mailto:yevhen.ruban@extrawest.com)
[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)]()
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![GitHub release](https://img.shields.io/badge/release-v1.0.0-blue)

The AI Video Insights Assistant is a powerful backend service that uses advanced AI orchestration to analyze YouTube video content and answer user-submitted questions in a contextually accurate manner. Built on either LangChain4j or Spring AI for robust orchestration, this app leverages embeddings and similarity search for precise content-based responses, while also including fallback options for comprehensive coverage.





https://github.com/user-attachments/assets/c85e4a9a-3e0f-4fbb-95e6-f3e371a74ae9





## Key Features

- **Robust Transcript Processing with Error Handling**: The assistant retrieves and processes video transcripts, generating meaningful answers based on video content. If the transcript cannot be processed, users receive clear, actionable error messages.
- **Embedding and Vector Storage**: Video content embeddings are stored in a vector database for efficient retrieval.
- **Similarity Search for Relevant Answers**: The assistant performs similarity searches on stored embeddings to identify the most relevant answers to user questions. If the relevance of all matching documents falls below a cosine similarity threshold of 0.75, a fallback mechanism activates.
- **Dynamic and User-Friendly API**: Designed with intuitive API, users can smoothly browse collections, select pieces of interest, and read detailed descriptions. The UI ensures that each piece of information is easy to access and visually engaging.
- **Fallback to Tavily API Search**: For low-relevance document matches, the assistant queries the Tavily API, using the “Include Answer” option to retrieve high-quality answers along with the top 3 related URLs from Tavily results, ensuring that users receive the most comprehensive response available.
- **Advanced Q&A on Video Content**: Users can submit any question about a video, and the assistant respond using contextually appropriate information derived from the video transcript.
- **Dual Data Source Configuration for AI Content Retrieval**: This feature enables AI to access information from two distinct data sources, each mapped to a unique identifier for efficient retrieval and context-specific responses.

## Tech Stack

- **Java 21**
- **SpringBoot 3.3.3**: Backend framework for building fast and scalable applications.
- **Tavily API**: Tavily is a search engine tailored for AI agents.
- **Qdrant**: Open Source and Purpose Built — Qdrant Filters Enable You to Apply Arbitrary Business Logic on Top of a Similarity Search.
- **Together AI**: Provides models for describe image. Model: Meta Llama 3.2 90B Vision Instruct Turbo
- **Langchain4j**: Supercharge your Java application with the power of LLMs.

## Running On Local Machine (Linux):

1. Set up the following environment variables.
    - export QDRANT_GRPC_HOST=your_host;
    - export QDRANT_API_KEY=your_api_key;
    - export TOGETHER_AI_API_KEY=your_api_key;
    - export TAVILY_API_KEY=your_api_key;
2. Run the command: mvn exec:java -Dspring.profiles.active=local
3. Open the following link in your browser: http://localhost:8208/api/swagger-ui/index.html#/

## Contributing

Feel free to open issues or submit pull requests to improve the project. Contributions are welcome!
