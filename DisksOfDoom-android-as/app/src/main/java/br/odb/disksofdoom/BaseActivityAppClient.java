package br.odb.disksofdoom;

import android.app.Activity;

import br.odb.gameapp.ApplicationClient;
import br.odb.utils.FileServerDelegate;

/**
 * Created by monty on 5/16/15.
 */
public class BaseActivityAppClient extends Activity implements ApplicationClient {
    @Override
    public void setClientId(String s) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void printWarning(String s) {

    }

    @Override
    public void printError(String s) {

    }

    @Override
    public void printVerbose(String s) {

    }

    @Override
    public String requestFilenameForSave() {
        return null;
    }

    @Override
    public String requestFilenameForOpen() {
        return null;
    }

    @Override
    public String getInput(String s) {
        return "";
    }

    @Override
    public int chooseOption(String s, String[] strings) {
        return 0;
    }

    @Override
    public FileServerDelegate getFileServer() {
        return null;
    }

    @Override
    public void printNormal(String s) {

    }

    @Override
    public void alert(String s) {

    }

    @Override
    public void playMedia(String s, String s1) {

    }

    @Override
    public void sendQuit() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public String openHTTP(String s) {
        return null;
    }

    @Override
    public void shortPause() {

    }
}
