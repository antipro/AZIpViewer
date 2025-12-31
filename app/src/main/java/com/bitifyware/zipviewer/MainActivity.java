package com.bitifyware.zipviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Main activity for ZipViewer - Private Archive Viewer
 * Features:
 * - Privacy First: All data stored in internal storage
 * - Encrypted Archives: Support for password-protected ZIP files
 * - Grid and List views
 * - Zip Editor: Can be called by other apps to open ZIP files
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fabViewToggle;
    private boolean isGridView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fabViewToggle = findViewById(R.id.fabViewToggle);

        // Set initial layout as grid view
        updateLayoutManager();

        // Toggle between grid and list view
        fabViewToggle.setOnClickListener(v -> {
            isGridView = !isGridView;
            updateLayoutManager();
            Toast.makeText(this, isGridView ? "Grid View" : "List View", Toast.LENGTH_SHORT).show();
        });

        // Check if activity was launched with a file intent
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handle incoming intents from other apps (like Telegram) to open ZIP files
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
                openArchiveFile(uri);
            }
        }
    }

    /**
     * Open and process archive file from URI
     * Stores file in internal storage for privacy
     */
    private void openArchiveFile(Uri uri) {
        try {
            // Copy file to internal storage for privacy
            File internalFile = copyToInternalStorage(uri);
            
            // TODO: Extract and display images from archive
            Toast.makeText(this, "Opening archive: " + uri.getLastPathSegment(), Toast.LENGTH_LONG).show();
            
        } catch (Exception e) {
            Toast.makeText(this, "Error opening archive: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Copy file to internal storage to ensure privacy
     * Files in internal storage cannot be accessed by other apps
     */
    private File copyToInternalStorage(Uri uri) throws Exception {
        File internalDir = new File(getFilesDir(), "archives");
        if (!internalDir.exists()) {
            internalDir.mkdirs();
        }
        
        // Get filename and sanitize to prevent path traversal
        String fileName = uri.getLastPathSegment();
        if (fileName == null || fileName.isEmpty()) {
            fileName = "archive_" + System.currentTimeMillis() + ".zip";
        }
        // Sanitize filename to prevent directory traversal attacks
        fileName = new File(fileName).getName();
        
        File outputFile = new File(internalDir, fileName);
        
        // Use try-with-resources to ensure streams are properly closed
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            
            if (inputStream == null) {
                throw new Exception("Cannot open input stream");
            }
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        
        return outputFile;
    }

    /**
     * Update RecyclerView layout between grid and list view
     */
    private void updateLayoutManager() {
        if (isGridView) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}
