# android-stt
Android Speech-to-Text (STT) using Google APIs.  
This class initializes the necessary Google speech recognition APIs, including the SpeechRecognizer object, with the relevant parameters. It enables the user to significantly reduce the code needed to achieve simple mobile speech-to-text functionality.

## Overview
In the class constructor, a SpeechRecognizer object is instantiated with the user-specified language model.  
A custom RecognitionListener, which implements the interface methods (onReadyForSpeech(), onBeginningOfSpeech(), onRmsChanged(), onBufferReceived(), onEndOfSpeech(), onError(), onResults(), onPartialResults(), and onEvent()) is passed in as the listener. The onError() method creates a toast upon an error specifying its type, and the OnResults() method creates the recognition result String to be retrieved.

## Usage
### Constructor:
**_public SpeechToText(Context context, String languageModel)_**  
Initialize a SpeechToText object.  
context - current application context.  
languageModel - specifies the model depending on desired use (either SpeechToText.WORD or SpeechToText.SENTENCE).

### Public Methods:
**_public void setProgressLogging(boolean enableLogging)_**  
Specify whether to enable logging of recognizer progress.  
enableLogging - _true_ to enable logging, and _false_ to disable (default).

**_recognize()_**  
Initiate speech recognition.

### Public Interface:
**_public interface SpeechToTextListener_**  
Interface for retrieving recognition result.

**_onRecognitionResult(String recognitionResult)_**  
Callback method for the recognize() method.  
recognitionResult - string result for the performed recognition.

## Example Usage
SpeechToText stt = new SpeechToText(this, SpeechToText.SENTENCE);  
stt.setProgressLogging(true);  
stt.recognize();  

@Override  
public void onRecognitionResult(String recognitionResult) {  
&nbsp;&nbsp;&nbsp;&nbsp;mTextView.setText(recognitionResult;  
}
