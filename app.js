const { app, BrowserWindow } = require('electron')

const browserWindowOptions = {
    title: 'Stury Audit',
    width: 900,
    height: 700,
    center: true,
    frame: true,
    resizeable: true,
    webPreferences: {
        nodeIntegration: true,
        contextIsolation: false,
        webSecurity: false,
        allowRunningInsecureContent: false
    }
}

function isDev()
{
  return !app.isPackaged;
}

//------------------------------------------------------------------------------
// App ready
//------------------------------------------------------------------------------


app.on('ready', () => {
  let mainWindow = new BrowserWindow(browserWindowOptions);

  if (isDev())
  {
    mainWindow.loadURL('http://localhost:8280/index.html')
  }
  else
  {
    mainWindow.loadURL('file://' + __dirname + '/resources/public/index.html')
  }


  mainWindow.on('closed', () => {
    mainWindow = null
    app.quit()
  })

  if (isDev())
  {
    mainWindow.toggleDevTools();
  }
})