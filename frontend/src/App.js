import React, { useState } from 'react';
import './App.css';

function App() {
  const [question, setQuestion] = useState('');
  const [response, setResponse] = useState('');

  const exampleQuestions = [
    "What is included in my Northwind Health Plus plan that is not in standard?",
    "What happens in a performance review?",
    "What does a Product Manager do?"
  ];

  const handleExampleClick = (example) => {
    setQuestion(example);
  };

  const askOpenAI = async () => {
    setResponse('Thinking...');

    try {
      const res = await fetch(`/ai/generate?message=${encodeURIComponent(question)}`);
      if (res.ok) {
        const data = await res.json();
        setResponse(data.generation);
      } else {
        setResponse('Error: Unable to get response from OpenAI');
      }
    } catch (error) {
      setResponse(`Error: ${error.message}`);
    }
  };

  const handleFileUpload = async (event) => {
    const file = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('file', file);

      try {
        const res = await fetch('/api/upload', {
          method: 'POST',
          body: formData,
        });
        if (res.ok) {
          const data = await res.json();
          setResponse(`File uploaded successfully.`);
        } else {
          setResponse('Error: Unable to upload file');
        }
      } catch (error) {
        setResponse(`Error: ${error.message}`);
      }
    }
  };

  return (
    <div className="App">
      <div id="chat-container">
        <h1>Chat with your data</h1>
        <p>Ask anything or try an example</p>
        <div className="example-questions">
          {exampleQuestions.map((example, index) => (
            <button key={index} onClick={() => handleExampleClick(example)}>{example}</button>
          ))}
        </div>
        <div id="input-container">
          <input
            type="file"
            onChange={handleFileUpload}
            className="file-upload"
          />
          <div id="question-input">
            <input
              type="text"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              placeholder="Type a new question (e.g. does my plan cover annual eye exams?)"
            />
            <button onClick={askOpenAI}>Ask</button>
          </div>
        </div>
        <div id="response">{response}</div>
      </div>
    </div>
  );
}

export default App;
