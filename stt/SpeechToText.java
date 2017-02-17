import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by alanchern on 10/16/16.
 * Speech-to-Text (STT) using Google APIs.
 */

public class SpeechToText {

    public static final String WORD = RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH;
    public static final String SENTENCE = RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;

    private static final String TAG = "SpeechToText";

    private Context mContext;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mRecognizerIntent;
    private boolean mEnableLogging = false;
    private String mRecognitionResult;

    /**
     * @param context current context
     * @param languageModel desired recognition language model, must be either
     *                      RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH or RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
     * @exception InvalidLanguageModelException
     */
    public SpeechToText(Context context, String languageModel) {
        mContext = context;

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext);
        mSpeechRecognizer.setRecognitionListener(new Listener());

        mRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        if (languageModel.equals(WORD)) {
            mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, WORD);
        } else if (languageModel.equals(SENTENCE)) {
            mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, SENTENCE);
        } else {
            throw (new InvalidLanguageModelException("Invalid language model"));
        }

        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
    }

    /**
     * Initiate speech recognition.
     */
    public void recognize() {
        mSpeechRecognizer.startListening(mRecognizerIntent);
    }

    /**
     * Enable speech recognizer progress logging information.
     * @param enableLogging true to enable logging, and false to disable (default)
     */
    public void setProgressLogging(boolean enableLogging) {
        mEnableLogging = enableLogging;
    }

    private class Listener implements RecognitionListener {

        @Override
        public void onReadyForSpeech(Bundle bundle) {
            if (mEnableLogging) {
                Log.d(TAG, "onReadyForSpeech");
            }
        }

        @Override
        public void onBeginningOfSpeech() {
            if (mEnableLogging) {
                Log.d(TAG, "onBeginningOfSpeech");
            }
        }

        @Override
        public void onRmsChanged(float v) {
            // Log.d(TAG, "onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            if (mEnableLogging) {
                Log.d(TAG, "onBufferReceived");
            }
        }

        @Override
        public void onEndOfSpeech() {
            if (mEnableLogging) {
                Log.d(TAG, "onEndofSpeech");
            }
        }

        @Override
        public void onError(int error) {
            if (mEnableLogging) {
                Log.d(TAG, "onError: " + error);
            }

            // handle recognition error
            switch (error) {
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    Toast.makeText(mContext, "Network timeout error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_AUDIO:
                    Toast.makeText(mContext, "Audio error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    Toast.makeText(mContext, "Server error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    Toast.makeText(mContext, "Client error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    Toast.makeText(mContext, "No speech input", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    Toast.makeText(mContext, "No match found", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    Toast.makeText(mContext, "Recognizer busy error", Toast.LENGTH_SHORT).show();
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    Toast.makeText(mContext, "Insufficient permissions error", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }

            mSpeechRecognizer.cancel();
        }

        @Override
        public void onResults(Bundle bundle) {
            if (mEnableLogging) {
                Log.d(TAG, "onResults");
            }

            // retrieve recognition result
            String recognitionResult = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).get(0);
            if (recognitionResult == null) {
                recognitionResult = "";
            }

            if (mContext instanceof SpeechToTextListener) {
                ((SpeechToTextListener) mContext).onRecognitionResult(recognitionResult);
            }
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            // not useful
        }

        @Override
        public void onEvent(int eventType, Bundle bundle) {
            if (mEnableLogging) {
                Log.d(TAG, "onEvent: " + eventType);
            }
        }
    }

    /**
     * Interface for retrieving recognition result.
     */
    public interface SpeechToTextListener {
        /**
         * Callback method for the recognize() method.
         * @param recognitionResult string result for the performed recognition
         */
        void onRecognitionResult(String recognitionResult);
    }

    private class InvalidLanguageModelException extends RuntimeException {
        private InvalidLanguageModelException(String message) {
            super(message);
        }
    }
}
