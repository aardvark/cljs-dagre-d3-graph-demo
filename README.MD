# ClojureScript Examples

Examples multiple graph drawing techniquies build through ClojureScript.
Note that there is explicit dependency on D3v5.
D3V6 and above are using some babel transformation
I am not sure how to currenlty work around them.

## Demo

Devcards demos are available here: <https://aardvark.github.io/cljs-dagre-d3-graph-demo/>

## Run

```bash
npx shadow-cljs watch :devcards
```

This will run a devcards servers over localhost:3001

## Release docs

Devcards github pages examples can be "released" by running next commands.
Using bash:

```bash
npx shadow-cljs release devcards
cp out/devcards/js/test.js docs/js/test.js
```

Using powershell:

```powershell
shadow-cljs release devcards
Copy-Item .\out\devcards\js\test.js .\docs\js\test.js
```

Or using babashka

```babashka
bb release-docs
```

## LICENSE

Licensed under MIT
