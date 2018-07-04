double blah(double x) {
  return x;
}

int main(int argc, char** argv) {
  double x = 20.0;
  x = blah(x);
  printf("%f", x);
}

