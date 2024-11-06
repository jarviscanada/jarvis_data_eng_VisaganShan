#!/bin/bash

# cd to script directory
cd "$(dirname "$0")"

# Function to download files using wget or curl, whichever is available
function download_file() {
  url=$1
  output=$2
  if command -v wget >/dev/null 2>&1; then
    wget "$url" -O "$output"
  elif command -v curl >/dev/null 2>&1; then
    curl -L "$url" -o "$output"
  else
    echo "Error: Neither wget nor curl is available. Please install one of them."
    exit 1
  fi
}

function init() {
  echo "---- Downloading sample profile.yaml file ----"
  download_file "https://raw.githubusercontent.com/jarviscanada/jarvis_profile_builder/develop/profile.yaml" "profile.yaml"
  exit 0
}

function check_status() {
  exit_code=$1
  if [ ${exit_code} -eq 0 ]; then
    echo "Success!!!👍"
    echo ""
  else
    echo "Failed"
    exit 1
  fi
}

function validate_yaml() {
  echo "---- Validating profile.yaml file ----"

  # Ensure schema file is downloaded locally
  if [[ ! -f profile_schema.yaml ]]; then
    echo "---- Downloading profile_schema.yaml ----"
    download_file "https://raw.githubusercontent.com/jarviscanada/jarvis_profile_builder/develop/profile_schema.yaml" "profile_schema.yaml"
  fi

  docker pull jrvs/yamale
  docker pull mikefarah/yq:3.3.4

  # Run validation with the local schema file
  docker run --rm -v "/$PWD":/workdir jrvs/yamale yamale -s /workdir/profile_schema.yaml /workdir/profile.yaml
  check_status $?
}

function get_profile_name() {
  echo "---- Parsing metadata ----"
  # Use winpty to enable compatibility with Git Bash on Windows
  profile_name=$(winpty docker run -it --rm -v "/$PWD":/workdir mikefarah/yq:3.3.4 yq r profile.yaml name | xargs | tr -d '\r' | sed -e 's/ /_/g')
  profile_prefix=jarvis_profile_${profile_name}
  echo ${profile_name}
  check_status $?
}

function yaml_to_json() {
  echo "---- Converting profile YAML to JSON ----"
  docker run --rm -v "/$PWD":/workdir mikefarah/yq:3.3.4 yq r -j --prettyPrint profile.yaml > profile.json
  check_status $?
}

function render_md() {
  echo "---- Rendering profile.md ----"
  docker pull jrvs/render_profile_md
  # Use winpty for interactive Docker run
  winpty docker run --rm -it -v "/$PWD":/workdir jrvs/render_profile_md profile.yaml profile.md
  check_status $?
}

function render_pdf() {
  echo "---- Rendering profile.pdf ----"
  template_profile=profile.md
  output_profile_pdf=${profile_prefix}.pdf

  top_bot_margin=1.75cm
  left_right_margin=1.5cm
  font_size=8

  docker run --rm --volume "/$PWD:/data" --user $(id -u):$(id -g) pandoc/latex:2.9.2.1 \
    ${template_profile} -f markdown -t pdf -s \
    --pdf-engine=xelatex -V pagestyle=empty -V fontsize=${font_size}pt -V geometry:"top=${top_bot_margin}, bottom=${top_bot_margin}, left=${left_right_margin}, right=${left_right_margin}" -o ${output_profile_pdf}
  check_status $?
}

function overwrite_readme() {
  if [[ -f ../README.md ]]; then
    echo "---- Moving profile.md to ../README.md ----"
    mv -f profile.md ../README.md
  fi
}

# Entry point for script
if [ "$1" = "init" ]; then
  init
fi

validate_yaml
get_profile_name
yaml_to_json
render_md
render_pdf
overwrite_readme

echo "Done!"
exit 0
