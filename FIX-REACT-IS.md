# Fix for Recharts / react-is

The frontend now explicitly includes `react-is`, which Recharts requires.

After extracting the project:

```powershell
cd frontend
npm install
npm run dev
```

If Vite still shows the old dependency error, run:

```powershell
Remove-Item -Recurse -Force node_modules
Remove-Item -Recurse -Force .vite -ErrorAction SilentlyContinue
Remove-Item -Force package-lock.json -ErrorAction SilentlyContinue
npm install
npm run dev
```

If PowerShell refuses the commands, delete `node_modules` manually and run `npm install` again.
