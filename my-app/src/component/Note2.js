const express = require('express');
const app = express();
const cors = require('cors');
const MongoDB = require('./db');
const { exec } = require('child_process');

MongoDB();

app.use(cors());
app.use(express.json());

app.get('/', (req, res) => {
  res.send('Hello World!');
});

// ...

app.post('/run-command', (req, res) => {
  const { tool, apk } = req.body;

  if (tool === 'Androwarn') {
    const androwarnPath = 'C:\\Users\\Admin\\Desktop\\androwarn';
    const command = `python ${androwarnPath} ${apk.path}`;

    exec(command, { stdio: 'pipe' }, (error, stdout, stderr) => {
      if (error) {
        console.error(error);
        return res.status(500).json({ error: 'Failed to execute command' });
      }
      
      console.log('Command Output:', stdout);
      console.error('Command Error:', stderr);
      
      res.json({ result: stdout });
    });
  } else if (tool === 'Androbugs') {
    // Handle Androbugs tool
    res.json({ result: 'Androbugs tool not implemented' });
  } else if (tool === 'Mobsf') {
    // Handle Mobsf tool
    res.json({ result: 'Mobsf tool not implemented' });
  } else {
    res.json({ result: 'Invalid tool selected' });
  }
});

const port = 4000;
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
